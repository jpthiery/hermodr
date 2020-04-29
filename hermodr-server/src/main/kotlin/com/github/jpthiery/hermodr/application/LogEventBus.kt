package com.github.jpthiery.hermodr.application

import io.quarkus.vertx.ConsumeEvent
import io.vertx.core.eventbus.Message
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class LogEventBus {

    private final val logger = LoggerFactory.getLogger(LogEventBus::class.java)

    @ConsumeEvent("application_error")
    fun consumeApplicationErrorEvent(message: Message<String>) {
        logger.error(message.body())
    }
    
    @ConsumeEvent("application.broadcaster")
    fun consumeApplicationBroadcasterEvent(message: Message<String>) {
        logger.info(message.body())
    }

}