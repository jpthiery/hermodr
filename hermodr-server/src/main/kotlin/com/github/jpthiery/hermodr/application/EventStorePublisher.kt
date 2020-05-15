package com.github.jpthiery.hermodr.application

import com.github.jpthiery.hermodr.domain.*
import io.vertx.core.eventbus.EventBus

class EventStorePublisher(
        private val delegate: EventStore,
        private val eventBus: EventBus
) : EventStore {
    override fun <C : Command, S : State, E : Event> getEventForAggregate(aggregate: Aggregate<C, S, E>, id: StreamId): List<E> =
            delegate.getEventForAggregate(aggregate, id)

    override fun <I : StreamId, C : Command, S : State, E : Event> appendEvents(aggregate: Aggregate<C, S, E>, id: I, events: List<E>, initialStateVersion: Int): AppendedEventResult {
        val res = delegate.appendEvents(aggregate, id, events, initialStateVersion)
        if (res is SuccessfulAppendedEventResult) {
            events.forEach { event ->
                eventBus.publish(BusAddresses.DOMAIN_EVENT.address, event)
            }
        }
        return res
    }
}