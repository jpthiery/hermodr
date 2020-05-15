package com.github.jpthiery.hermodr.application.configuration

import com.github.jpthiery.hermodr.application.CommandHandler
import com.github.jpthiery.hermodr.application.DomainBusCodec
import com.github.jpthiery.hermodr.domain.*
import com.github.jpthiery.hermodr.infra.broadcaster.icecast.BindingLibShoutProvider
import com.github.jpthiery.hermodr.infra.broadcaster.icecast.IcecastConfiguration
import com.github.jpthiery.hermodr.infra.musicsource.MusicFileLocator
import com.github.jpthiery.hermodr.infra.projection.ListSharedRadio
import com.github.jpthiery.hermodr.infra.projection.MusicDetailsRepository
import com.github.jpthiery.hermodr.infra.projection.SharedRadioDetails
import io.quarkus.runtime.StartupEvent
import io.vertx.core.Vertx
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.Dependent
import javax.enterprise.event.Observes
import javax.enterprise.inject.Produces
import javax.inject.Singleton

@Dependent
class ServiceConfiguration {

    @ConfigProperty(name = "broadcaster.icecast.libshoutPath")
    lateinit var libshoutPath: String

    @ConfigProperty(name = "broadcaster.icecast.host")
    lateinit var icecastHost: String

    @ConfigProperty(name = "broadcaster.icecast.port")
    var icecastPort: Int = 8000

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
        vertx.deployVerticle(musicDetailsRepository)
    }

    fun startVertx(@Observes startupEvent: StartupEvent, vertx: Vertx) {
        val eventBus = vertx.eventBus()

        eventBus.registerDefaultCodec(SharedRadioId::class.java, DomainBusCodec(SharedRadioId::class.java))
        eventBus.registerDefaultCodec(MusicId::class.java, DomainBusCodec(MusicId::class.java))

        eventBus.registerDefaultCodec(CreateSharedRadio::class.java, DomainBusCodec(CreateSharedRadio::class.java))
        eventBus.registerDefaultCodec(AddMusicToSharedRadio::class.java, DomainBusCodec(AddMusicToSharedRadio::class.java))
        eventBus.registerDefaultCodec(ValidateMusicToSharedRadio::class.java, DomainBusCodec(ValidateMusicToSharedRadio::class.java))
        eventBus.registerDefaultCodec(StartMusicToSharedRadio::class.java, DomainBusCodec(StartMusicToSharedRadio::class.java))
        eventBus.registerDefaultCodec(EndMusicToSharedRadio::class.java, DomainBusCodec(EndMusicToSharedRadio::class.java))

        eventBus.registerDefaultCodec(SharedRadioCreated::class.java, DomainBusCodec(SharedRadioCreated::class.java))
        eventBus.registerDefaultCodec(MusicAdded::class.java, DomainBusCodec(MusicAdded::class.java))
        eventBus.registerDefaultCodec(MusicValidated::class.java, DomainBusCodec(MusicValidated::class.java))
        eventBus.registerDefaultCodec(MusicStarted::class.java, DomainBusCodec(MusicStarted::class.java))
        eventBus.registerDefaultCodec(MusicFinished::class.java, DomainBusCodec(MusicFinished::class.java))
        eventBus.registerDefaultCodec(MusicEnded::class.java, DomainBusCodec(MusicEnded::class.java))

        eventBus.registerDefaultCodec(SuccessfullyHandleCommand::class.java, DomainBusCodec(SuccessfullyHandleCommand::class.java))
        eventBus.registerDefaultCodec(FailedToHandleCommand::class.java, DomainBusCodec(FailedToHandleCommand::class.java))
        eventBus.registerDefaultCodec(NoopToHandleCommand::class.java, DomainBusCodec(NoopToHandleCommand::class.java))

    }

}