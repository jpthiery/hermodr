package com.github.jpthiery.hermodr.application.configuration

import com.github.jpthiery.hermodr.application.EventStorePublisher
import com.github.jpthiery.hermodr.domain.EventStore
import com.github.jpthiery.hermodr.infra.InMemoryEventStore
import io.vertx.core.eventbus.EventBus
import javax.enterprise.context.Dependent
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

}