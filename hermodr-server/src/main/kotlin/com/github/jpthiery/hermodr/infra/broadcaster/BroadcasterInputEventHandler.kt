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

    @Inject
    @field:Default
    lateinit var libShoutProvider: BindingLibShoutProvider

    @ConfigProperty(name = "radio.defaultMusic.path")
    lateinit var defaultMusicPath: String

    @ConfigProperty(name = "radio.defaultMusic.title")
    lateinit var defaultMusicTitle: String

    @ConfigProperty(name = "broadcaster.icecast.libshoutPath")
    lateinit var libshoutPath: String

    private val broadcasterActors = mutableMapOf<SharedRadioId, BroadcasterActor>()

    private val musicWaitingValidations = mutableMapOf<MusicId, String>()

    @ConsumeEvent("domain.event")
    fun consumeDomainEvent(message: Message<Event>) {
        val event = message.body()
        logger.info(event.toString())
        when (event) {
            is SharedRadioCreated -> {
                Music(
                        "${MusicScheme.LOCALFILE.scheme}/$defaultMusicPath".createMusicId(),
                        MusicScheme.LOCALFILE,
                        defaultMusicPath,
                        defaultMusicTitle
                )
                val playlist = BroadcastPlaylist()
                val icecastBroadcaster = IcecastBroadcaster(
                        event.id,
                        libShoutProvider.provide(),
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
                        logger.info("Icecast broadcaster started.")
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
