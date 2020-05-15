package com.github.jpthiery.hermodr.infra.projection

import com.github.jpthiery.hermodr.application.BusAddresses
import com.github.jpthiery.hermodr.domain.SharedRadioCreated
import com.github.jpthiery.hermodr.domain.SharedRadioEvent
import com.github.jpthiery.hermodr.domain.SharedRadioId
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ListSharedRadio : AbstractVerticle() {

    private val sharedRadios = mutableListOf<JsonObject>()

    override fun start() {
        vertx.eventBus()
                .consumer<SharedRadioEvent>(BusAddresses.DOMAIN_EVENT.address) { message ->
                    val event = message.body()
                    if (event is SharedRadioCreated) {
                        val radio = JsonObject()
                        radio.put("id", event.id.id)
                        radio.put("name", event.name)
                        sharedRadios.add(radio)
                    }
                }

        vertx.eventBus()
                .consumer<String>(BusAddresses.SHAREDRADIO_LIST.address) { message ->
                    val res = JsonArray()
                    sharedRadios.forEach {
                        res.add(it)
                    }
                    message.reply(res)
                }
    }

}