package com.github.jpthiery.hermodr.infra.broadcaster

import com.github.jpthiery.hermodr.domain.*
import com.github.jpthiery.hermodr.infra.broadcaster.icecast.BindingLibShoutProvider
import com.github.jpthiery.hermodr.infra.broadcaster.icecast.IcecastBroadcaster
import com.github.jpthiery.hermodr.infra.broadcaster.icecast.IcecastConfiguration
import io.quarkus.vertx.ConsumeEvent
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject

@ApplicationScoped
class BroadcasterInputEventHandler {

    private val logger = LoggerFactory.getLogger(BroadcasterInputEventHandler::class.java)

    @Inject
    @field:Default
    lateinit var vertx: Vertx

    @ConfigProperty(name = "radio.defaultMusic.path")
    lateinit var defaultMusicPath: String

    @ConfigProperty(name = "radio.defaultMusic.title")
    lateinit var defaultMusicTitle: String

    private val broadcasterActors = mutableMapOf<SharedRadioId, BroadcasterActor>()

    private val musicWaitingValidations = mutableMapOf<MusicId, String>()

    @ConsumeEvent("domain.event")
    fun consumeDomainEvent(message: Message<Event>) {
        val event = message.body()
        logger.info(event.toString())
        when (event) {
            is SharedRadioCreated -> {
                val config = IcecastConfiguration.newIcecastConfiguration()
                        .invoke("icecast.jpthiery.techx.fr", 8000)
                        .invoke(event.name)
                        .auth("source", "sourcejpthiery")
                        .sendMp3()
                        .useHttp()
                        .build()
                val bindingLibShoutProvider = BindingLibShoutProvider(
                        //"/home/jpthiery/workspace/perso/hermodr/broadcaster-icecast/target/broadcaster-icecast.so",
                        "/Users/jpthiery/workspace/broadcaster/broadcaster-icecast/target/broadcaster-icecast.so",
                        config
                )
                Music(
                        "${MusicScheme.LOCALFILE.scheme}/$defaultMusicPath".createMusicId(),
                        MusicScheme.LOCALFILE,
                        defaultMusicPath,
                        defaultMusicTitle
                )
                val playlist = BroadcastPlaylist()
                val icecastBroadcaster = IcecastBroadcaster(
                        event.id,
                        bindingLibShoutProvider.provide(),
                        Mp3MusicFile(
                                "${MusicScheme.LOCALFILE.scheme}/$defaultMusicPath".createMusicId(),
                                defaultMusicTitle,
                                defaultMusicPath
                        ),
                        playlist,
                        vertx.eventBus()
                )
                vertx.deployVerticle(icecastBroadcaster) { result ->
                    if (result.succeeded()) {
                        broadcasterActors[event.id] = BroadcasterActor(
                                event.id,
                                playlist,
                                icecastBroadcaster,
                                result.result()
                        )
                    }
                }
            }
            is MusicAdded -> {
                if (!musicWaitingValidations.containsKey(event.music.id)) {
                    musicWaitingValidations[event.music.id] = event.music.title
                }
            }
            is MusicValidated -> {
                if (broadcasterActors.containsKey(event.id) && musicWaitingValidations.containsKey(event.music.id)) {
                    val broadcasterActor = broadcasterActors[event.id]!!
                    val title = event.music.title
                    broadcasterActor.playlist.addMusic(
                            Mp3MusicFile(
                                    event.music.id,
                                    title,
                                    event.music.location
                            )
                    )
                    musicWaitingValidations.remove(event.music.id)
                    logger.info("Added '${title}' to radio ${event.id}")
                } else {
                    logger.warn("Music validate for unknown radio ${event.id}.")
                }
            }
            else -> {
                logger.debug("Drop event ${event::class.java.simpleName}.")
            }
        }
    }

    data class BroadcasterActor(val sharedRadioId: SharedRadioId, val playlist: BroadcastPlaylist, val broadcaster: Broadcaster, val deployId: String)

}
