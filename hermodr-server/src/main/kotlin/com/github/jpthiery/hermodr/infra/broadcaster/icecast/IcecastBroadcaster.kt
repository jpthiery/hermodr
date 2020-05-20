package com.github.jpthiery.hermodr.infra.broadcaster.icecast

import com.github.jpthiery.hermodr.application.BusAddresses
import com.github.jpthiery.hermodr.domain.EndMusicToSharedRadio
import com.github.jpthiery.hermodr.domain.SharedRadioId
import com.github.jpthiery.hermodr.domain.StartMusicToSharedRadio
import com.github.jpthiery.hermodr.infra.broadcaster.BroadcastPlaylist
import com.github.jpthiery.hermodr.infra.broadcaster.Broadcaster
import com.github.jpthiery.hermodr.infra.broadcaster.MusicFile
import com.github.jpthiery.hermodr.infra.broadcaster.icecast.binding.BindingLibShout
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.eventbus.EventBus
import org.slf4j.LoggerFactory
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

class IcecastBroadcaster(
        private val sharedRadioId: SharedRadioId,
        private val bindingLibShout: BindingLibShout,
        private val defaultMusicFile: MusicFile,
        private val playlist: BroadcastPlaylist,
        private val eventBus: EventBus
) : AbstractVerticle(), Broadcaster {

    private val logger = LoggerFactory.getLogger(javaClass)

    private var currentMusic: MusicFile = defaultMusicFile;

    private var currentInputStream = readMusic(defaultMusicFile)

    private lateinit var t: Thread

    override fun start(startPromise: Promise<Void>?) {
        logger.info("Starting icecaste broadcaster with libshout version ${bindingLibShout.libVersion()}")
        vertx.runOnContext {
            t = Thread() {
                logger.debug("Starting thread to send music")
                if (bindingLibShout.isNotConnected) {
                    try {
                        val openResult = bindingLibShout.open()
                        openResult.errorMessage.ifPresent {
                            val message = "Failed to open icecast due to following error: $it"
                            logger.error(message)
                            eventBus.send(BusAddresses.APPLICATION_ERROR.address, message)
                        }
                    } catch (e: Throwable) {
                        logger.error("Unable to open icecast connection due to following error", e)
                        throw e
                    }
                } else {
                    logger.info("Icecast already connected.")
                }
                selectNextMusic()
                startPromise?.complete()
                val buffer = ByteArray(1024)
                while (!Thread.currentThread().isInterrupted) {
                    val read = currentInputStream.read(buffer)
                    if (read > 0) {
                        val sendResult = bindingLibShout.send(buffer, read)
                        if (!sendResult.isSuccess) {
                            sendResult.errorMessage.ifPresent {
                                eventBus.send(BusAddresses.APPLICATION_ERROR.address, "Unable to send music to icecast due to following error, stopping broadcasting : $it")
                            }
                            break
                        }
                    } else {
                        currentInputStream.close()
                        eventBus.send(
                                BusAddresses.DOMAIN_COMMAND.address,
                                EndMusicToSharedRadio(
                                        sharedRadioId,
                                        currentMusic.id
                                )
                        )
                        selectNextMusic()
                    }
                }
                logger.warn("Closing icecast broadcaster")
                bindingLibShout.close()
                currentInputStream.close()
            }
            t.start()
        }
    }

    override fun stop() {
        t.interrupt()
    }

    private fun readMusic(music: MusicFile) = BufferedInputStream(FileInputStream(File(music.filePath)))

    private fun selectNextMusic() {
        playlist.next()
                .fold(
                        {
                            currentMusic = defaultMusicFile
                            currentInputStream = readMusic(defaultMusicFile)
                        }
                ) {
                    currentMusic = it
                    currentInputStream = readMusic(currentMusic)
                }


        eventBus.send("application.broadcaster", "Select music '${currentMusic.title}' locate at ${currentMusic.filePath} .")
        eventBus.send(
                BusAddresses.DOMAIN_COMMAND.address,
                StartMusicToSharedRadio(
                        sharedRadioId,
                        currentMusic.id
                )
        )
        bindingLibShout.sendSongMetadata(currentMusic.title)
        logger.debug("Select next track ${currentMusic.title}.")
        logger.trace("icecast version lib ${bindingLibShout.libVersion()}.")
    }

    override fun skip() {
        TODO("Not yet implemented")
    }

}