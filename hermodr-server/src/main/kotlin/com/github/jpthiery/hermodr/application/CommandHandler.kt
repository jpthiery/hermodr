package com.github.jpthiery.hermodr.application

import com.github.jpthiery.hermodr.domain.*
import io.vertx.core.AbstractVerticle
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class CommandHandler(
        @Inject val eventStore: EventStore
) : AbstractVerticle() {

    private val logger = LoggerFactory.getLogger(CommandHandler::class.java)

    override fun start() {
        val cqrsEngine = CqrsEngine(eventStore)
        vertx.eventBus()
                .consumer<Command>(BusAddresses.DOMAIN_COMMAND.address)
                .handler { message ->
                    logger.info("Received command :${message.body()}")
                    val commandResult = when (val command = message.body()) {
                        is SharedRadioCommand -> {
                            logger.debug("Processing following command :$command")
                            val aggregate = SharedRadio()
                            cqrsEngine.handleCommand(aggregate, command)
                        }
                        else -> NoopToHandleCommand(command)
                    }

                    logger.debug("${message.body()} -> $commandResult")
                    message.reply(commandResult)
                }
    }
}