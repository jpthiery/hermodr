package com.github.jpthiery.hermodr.application.configuration

import com.github.jpthiery.hermodr.application.DomainBusCodec
import com.github.jpthiery.hermodr.application.EventStorePublisher
import com.github.jpthiery.hermodr.domain.*
import com.github.jpthiery.hermodr.infra.InMemoryEventStore
import io.quarkus.runtime.StartupEvent
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import javax.enterprise.context.Dependent
import javax.enterprise.event.Observes
import javax.enterprise.inject.Produces
import javax.inject.Singleton

@Dependent
class DomainConfiguration {

    @Produces
    @Singleton
    fun eventStore(eventBus: EventBus): EventStore {
        val inMemoryEventStore = InMemoryEventStore()
        return EventStorePublisher(
                inMemoryEventStore,
                eventBus
        )
    }

    fun startVertx(@Observes startupEvent: StartupEvent, vertx: Vertx) {
        val eventBus = vertx.eventBus()

        eventBus.registerDefaultCodec(SharedRadioId::class.java, DomainBusCodec(SharedRadioId::class.java))
        eventBus.registerDefaultCodec(MusicId::class.java, DomainBusCodec(MusicId::class.java))

        eventBus.registerDefaultCodec(CreateSharedRadio::class.java, DomainBusCodec(CreateSharedRadio::class.java))
        eventBus.registerDefaultCodec(AddMusicToSharedRadio::class.java, DomainBusCodec(AddMusicToSharedRadio::class.java))
        eventBus.registerDefaultCodec(RemovedMusicToSharedRadio::class.java, DomainBusCodec(RemovedMusicToSharedRadio::class.java))
        eventBus.registerDefaultCodec(ValidateMusicToSharedRadio::class.java, DomainBusCodec(ValidateMusicToSharedRadio::class.java))
        eventBus.registerDefaultCodec(StartMusicToSharedRadio::class.java, DomainBusCodec(StartMusicToSharedRadio::class.java))
        eventBus.registerDefaultCodec(EndMusicToSharedRadio::class.java, DomainBusCodec(EndMusicToSharedRadio::class.java))

        eventBus.registerDefaultCodec(SharedRadioCreated::class.java, DomainBusCodec(SharedRadioCreated::class.java))
        eventBus.registerDefaultCodec(MusicAdded::class.java, DomainBusCodec(MusicAdded::class.java))
        eventBus.registerDefaultCodec(MusicRemoved::class.java, DomainBusCodec(MusicRemoved::class.java))
        eventBus.registerDefaultCodec(MusicValidated::class.java, DomainBusCodec(MusicValidated::class.java))
        eventBus.registerDefaultCodec(MusicStarted::class.java, DomainBusCodec(MusicStarted::class.java))
        eventBus.registerDefaultCodec(MusicFinished::class.java, DomainBusCodec(MusicFinished::class.java))
        eventBus.registerDefaultCodec(MusicEnded::class.java, DomainBusCodec(MusicEnded::class.java))

        eventBus.registerDefaultCodec(SuccessfullyHandleCommand::class.java, DomainBusCodec(SuccessfullyHandleCommand::class.java))
        eventBus.registerDefaultCodec(FailedToHandleCommand::class.java, DomainBusCodec(FailedToHandleCommand::class.java))
        eventBus.registerDefaultCodec(NoopToHandleCommand::class.java, DomainBusCodec(NoopToHandleCommand::class.java))

    }

}