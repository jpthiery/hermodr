package com.github.jpthiery.hermodr.application.configuration

import com.github.jpthiery.hermodr.application.CommandHandler
import com.github.jpthiery.hermodr.domain.MusicId
import com.github.jpthiery.hermodr.infra.broadcaster.icecast.BindingLibShoutProvider
import com.github.jpthiery.hermodr.infra.broadcaster.icecast.IcecastConfiguration
import com.github.jpthiery.hermodr.infra.musicsource.MusicFileLocator
import com.github.jpthiery.hermodr.infra.projection.ListSharedRadio
import com.github.jpthiery.hermodr.infra.projection.MusicDetailsRepository
import com.github.jpthiery.hermodr.infra.projection.SharedRadioDetails
import io.quarkus.runtime.StartupEvent
import io.vertx.core.Vertx
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.LoggerFactory
import java.io.File
import javax.enterprise.context.Dependent
import javax.enterprise.event.Observes
import javax.enterprise.inject.Produces
import javax.inject.Singleton

@Dependent
class ServiceConfiguration {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ConfigProperty(name = "broadcaster.icecast.libshoutPath")
    lateinit var libshoutPath: String

    @ConfigProperty(name = "broadcaster.icecast.host")
    lateinit var icecastHost: String

    @ConfigProperty(name = "broadcaster.icecast.port")
    var icecastPort: Int = 8000

    @ConfigProperty(name = "radio.defaultMusic.upload.dir")
    lateinit var musicUploadedDirectory: String

    @Produces
    @Singleton
    fun bindingLibShoutProvider(): BindingLibShoutProvider {
        val config = IcecastConfiguration.newIcecastConfiguration()
                .invoke(icecastHost, icecastPort)
                .invoke("Hermodr")
                .auth("source", "sourcejpthiery")
                .sendMp3()
                .useHttp()
                .build()
        return BindingLibShoutProvider(
                libshoutPath,
                config
        )
    }

    @Produces
    @Singleton
    fun musicLocator(): MusicFileLocator = object : MusicFileLocator {
        override fun locate(musicId: MusicId): String = "/tmp/hermodr/${musicId}.mp3"
    }

    fun startbindingLibShoutProvider(@Observes startupEvent: StartupEvent, bindingLibShoutProvider: BindingLibShoutProvider) {
        val bindingLibShout = bindingLibShoutProvider.provide()
        logger.info("Binding Libshout use version {}", bindingLibShout.libVersion())
    }

    fun startCommandHandler(@Observes startupEvent: StartupEvent, vertx: Vertx, commandHandler: CommandHandler) {
        vertx.deployVerticle(commandHandler)
    }

    fun startSharedRadioDetail(@Observes startupEvent: StartupEvent, vertx: Vertx, sharedRadioDetails: SharedRadioDetails) {
        vertx.deployVerticle(sharedRadioDetails)
    }

    fun startListSharedRadio(@Observes startupEvent: StartupEvent, vertx: Vertx, listSharedRadio: ListSharedRadio) {
        vertx.deployVerticle(listSharedRadio)
    }

    fun startMusicDetailsRepository(@Observes startupEvent: StartupEvent, vertx: Vertx, musicDetailsRepository: MusicDetailsRepository) {
        val uploadedFileDirectory = File(musicUploadedDirectory)
        if (!uploadedFileDirectory.exists()) {
            uploadedFileDirectory.mkdirs()
        }
        vertx.deployVerticle(musicDetailsRepository)
    }

}