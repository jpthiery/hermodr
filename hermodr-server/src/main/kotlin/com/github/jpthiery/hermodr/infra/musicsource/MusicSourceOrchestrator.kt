package com.github.jpthiery.hermodr.infra.musicsource

import com.github.jpthiery.hermodr.application.BusAddresses
import com.github.jpthiery.hermodr.domain.*
import com.github.jpthiery.hermodr.infra.musicsource.local.LocalFileMusicSource
import com.github.jpthiery.hermodr.infra.musicsource.youtube.YoutubeMusicSource
import io.quarkus.vertx.ConsumeEvent
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.Executors
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject

@ApplicationScoped
class MusicSourceOrchestrator : AbstractVerticle() {

    private val logger = LoggerFactory.getLogger(MusicSourceOrchestrator::class.java)

    private val fetchingVerticle: MutableMap<MusicId, MusicSource> = mutableMapOf()

    private val fetchedMusic = mutableSetOf<Music>()

    private val executorService = Executors.newFixedThreadPool(4)

    @Inject
    @field: Default
    lateinit var musicFileLocator: MusicFileLocator

    @Inject
    @field: Default
    lateinit var eventBus: EventBus

    @ConsumeEvent("domain.event")
    fun consumeDomainEvent(message: Message<SharedRadioEvent>) {
        logger.info("Received ${message.body()}")
        val event = message.body()
        when (event) {
            is MusicAdded -> {
                val musicId = event.music.id
                fetchedMusic.find { it.id == musicId }
                        ?.let { musicAlreadyFetch ->
                            eventBus.send(
                                    BusAddresses.DOMAIN_COMMAND.address,
                                    ValidateMusicToSharedRadio(
                                            event.id,
                                            musicAlreadyFetch.id,
                                            musicAlreadyFetch.title,
                                            musicAlreadyFetch.scheme,
                                            musicAlreadyFetch.location
                                    )
                            )
                        }
                        ?: run {
                            val outputFilePath = musicFileLocator.locate(event.music.id)
                            val outputFile = File(outputFilePath)

                            if (!fetchingVerticle.containsKey(musicId)) {
                                val musicSource = when (event.music.scheme) {
                                    MusicScheme.YOUTUBE -> YoutubeMusicSource()
                                    MusicScheme.LOCALFILE -> LocalFileMusicSource()
                                }
                                runMusicSource(musicSource, event, outputFile)
                            } else {
                                logger.info("Already fetching music ${event.music.title} on ${event.music.scheme.scheme}.")
                            }
                        }
            }
            else -> {
                logger.debug("Drop event ${event::class.java.simpleName}.")
            }
        }
    }

    private fun runMusicSource(musicSource: MusicSource, musicAdded: MusicAdded, outputFile: File) {
        val task = {
            logger.info("Starting music source ${musicSource::class.java.simpleName}")
            logger.debug("Starting music source ${musicSource::class.java.simpleName} with input music ${musicAdded.music} to ${outputFile.absolutePath}.")
            musicSource.fetch(musicAdded.music, outputFile).fold(
                    { err ->
                        logger.error("Unable to fetch ${musicAdded.music}", err)
                        val command = RemovedMusicToSharedRadio(
                                musicAdded.id,
                                musicAdded.music.id
                        )
                        eventBus.send(
                                BusAddresses.DOMAIN_COMMAND.address,
                                command
                        )
                        logger.error("Request music to be delete: {}", command,  err)

                    },
                    { musicStore ->
                        logger.info("Successfully fetch ${musicAdded.music}")
                        fetchedMusic.add(musicStore)
                        eventBus.send(
                                BusAddresses.DOMAIN_COMMAND.address,
                                ValidateMusicToSharedRadio(
                                        musicAdded.id,
                                        musicAdded.music.id,
                                        musicStore.title,
                                        musicStore.scheme,
                                        musicStore.location,
                                        musicStore.artist,
                                        musicStore.album
                                )
                        )
                    }
            )
            logger.debug("Processing ${musicAdded.music} done.")
        }
        fetchingVerticle[musicAdded.music.id] = musicSource
        executorService.submit(task)
    }


}