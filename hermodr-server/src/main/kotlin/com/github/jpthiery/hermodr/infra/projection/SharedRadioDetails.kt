package com.github.jpthiery.hermodr.infra.projection

import com.github.jpthiery.hermodr.application.BusAddresses
import com.github.jpthiery.hermodr.domain.*
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.LoggerFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SharedRadioDetails : AbstractVerticle() {

    private val logger = LoggerFactory.getLogger(SharedRadioDetails::class.java)

    private val sharedRadioRepository = mutableMapOf<SharedRadioId, SharedRadioView>()

    @ConfigProperty(name = "broadcaster.icecast.baseUrlToListen")
    lateinit var icecastBaseUrlToListen: String

    @ConfigProperty(name = "radio.defaultMusic.path")
    lateinit var defaultMusicPath: String

    @ConfigProperty(name = "radio.defaultMusic.title")
    lateinit var defaultMusicTitle: String

    override fun start() {
        vertx.eventBus()
                .consumer<SharedRadioEvent>(BusAddresses.DOMAIN_EVENT.address) { message ->
                    when (val event = message.body()) {
                        is SharedRadioCreated -> sharedRadioRepository[event.id] = SharedRadioView(
                                event.id.id,
                                event.name,
                                emptyList(),
                                null
                        )
                        is MusicValidated -> {
                            val view = sharedRadioRepository[event.id]
                            view?.let {
                                val newPlaylist = it.playlist.toMutableList()
                                newPlaylist.add(event.music)
                                val newRadioView = view.copy(
                                        playlist = newPlaylist.toList()
                                )
                                logger.info("New radio view $newRadioView")
                                sharedRadioRepository[event.id] = newRadioView
                            }
                        }
                        is MusicStarted -> {
                            sharedRadioRepository[event.id]?.let { view ->
                                val newPlaylist = view.playlist.toMutableList()
                                val music = view.playlist.find {
                                    it.id == event.musicId
                                }
                                music?.let {
                                    newPlaylist.remove(it)
                                }
                                logger.info("Define current play to music $music .")
                                sharedRadioRepository[event.id] = view.copy(
                                        playlist = newPlaylist.toList(),
                                        currentPlay = music
                                )
                            }
                        }
                        else -> logger.debug("Drop event $event")
                    }
                }
        vertx.eventBus()
                .consumer<SharedRadioId>(BusAddresses.SHAREDRADIO_DETAIL.address) { message ->
                    val sharedRadioId = message.body()
                    sharedRadioRepository[sharedRadioId]?.let { view ->
                        val json = JsonObject()
                        json.put("id", view.id)
                        json.put("name", view.name)
                        val encodedName = URLEncoder.encode(view.name, StandardCharsets.UTF_8)
                        val urlToListen = "$icecastBaseUrlToListen/${encodedName}.m3u"
                        json.put("urlToListen", urlToListen)
                        view.currentPlay?.let {
                            json.put("currentPlay", convertMusicToJson(it))
                        } ?: json.put("currentPlay", getDefaultMusic())
                        val playlistJson = JsonArray()
                        view.playlist.forEach {
                            val element = convertMusicToJson(it)
                            playlistJson.add(element)
                        }
                        json.put("playlist", playlistJson)
                        message.reply(json)
                    }
                }
    }

    private fun convertMusicToJson(it: Music): JsonObject {
        val element = JsonObject()
        element.put("id", it.id)
        element.put("title", it.title)
        element.put("source", it.scheme.name)
        element.put("artist", it.artist)
        element.put("album", it.album)
        if (it.scheme != MusicScheme.LOCALFILE) element.put("location", it.location)
        if (it.duration > 0) element.put("duration", it.duration)
        element.put("validated", true)
        return element
    }

    private fun getDefaultMusic(): JsonObject {
        val element = JsonObject()
        element.put("id", MusicScheme.LOCALFILE.scheme + defaultMusicPath.createMusicId())
        element.put("title", defaultMusicTitle)
        element.put("source", MusicScheme.LOCALFILE.name)
        element.put("validated", true)
        return element
    }

    data class SharedRadioView(
            val id: String,
            val name: String,
            val playlist: List<Music>,
            val currentPlay: Music?
    )

}