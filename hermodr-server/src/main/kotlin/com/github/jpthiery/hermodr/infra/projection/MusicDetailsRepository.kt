package com.github.jpthiery.hermodr.infra.projection

import com.github.jpthiery.hermodr.application.BusAddresses
import com.github.jpthiery.hermodr.domain.*
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MusicDetailsRepository : AbstractVerticle() {

    private val logger = LoggerFactory.getLogger(MusicDetailsRepository::class.java)

    private val musicEventListened = setOf(
            MusicAdded::class.java,
            MusicValidated::class.java
    )

    private val musics = mutableMapOf<MusicId, JsonObject>()

    override fun start() {
        logger.info("Starting")
        vertx.eventBus()
                .consumer<SharedRadioEvent>(BusAddresses.DOMAIN_EVENT.address) {
                    val event = it.body()
                    if (musicEventListened.contains(event::class.java)) {
                        val json = convertEventToMusicJson(event)
                        logger.info("Adding following music : $json")
                        musics[extractMusicIdFromEvent(event)] = json
                    }
                }

        vertx.eventBus()
                .consumer<MusicId>(BusAddresses.MUSIC_DETAIL.address) {
                    val musicId = it.body()
                    val musicDetail = musics[musicId] ?: JsonObject()
                    logger.info("Found following music for requested id $musicId : $musicDetail")
                    it.reply(musicDetail)
                }
    }

    private fun extractMusicIdFromEvent(event: SharedRadioEvent) =
            when (event) {
                is MusicAdded -> event.music.id
                is MusicValidated -> event.music.id
                else -> "Unknown".createMusicId()
            }

    private fun convertEventToMusicJson(event: SharedRadioEvent): JsonObject {
        val res = musics[extractMusicIdFromEvent(event)] ?: JsonObject()
        when (event) {
            is MusicAdded -> {
                res.put("id", event.music.id)
                res.put("title", event.music.title)
                res.put("source", event.music.scheme.name)
                if (event.music.scheme != MusicScheme.LOCALFILE) res.put("location", event.music.location)
            }
            is MusicValidated -> {
                res.put("id", event.music.id)
                res.put("title", event.music.title)
                res.put("artist", event.music.artist)
                res.put("album", event.music.album)
                res.put("validated", true)
            }
            else -> {}
        }
        return res
    }

}