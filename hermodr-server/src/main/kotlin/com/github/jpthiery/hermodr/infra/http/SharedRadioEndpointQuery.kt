package com.github.jpthiery.hermodr.infra.http

import com.github.jpthiery.hermodr.application.BusAddresses
import com.github.jpthiery.hermodr.domain.*
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.mutiny.core.eventbus.EventBus
import org.jboss.resteasy.annotations.SseElementType
import org.slf4j.LoggerFactory
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/sharedRadio/query")
class SharedRadioEndpointQuery {

    @Inject
    @field: Default
    lateinit var eventBus: EventBus

    private val logger = LoggerFactory.getLogger(SharedRadioEndpointQuery::class.java)

    @Path("/{sharedRadioId}/events")
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    fun event(@PathParam("sharedRadioId") id: String): Multi<JsonObject> {
        val sharedRadioId = SharedRadioId(id)
        return eventBus
                .consumer<SharedRadioEvent>(BusAddresses.DOMAIN_EVENT.address)
                .toMulti()
                .map {
                    it.body()
                }
                .transform().byFilteringItemsWith { event -> event.id == sharedRadioId }

                .map {
                    when (it) {
                        is MusicValidated -> SharedRadioEventWrapper(it, it.music.id)
                        is MusicAdded -> SharedRadioEventWrapper(it, it.music.id)
                        is MusicRemoved -> SharedRadioEventWrapper(it, it.musicId)
                        is MusicStarted -> SharedRadioEventWrapper(it, it.musicId)
                        is MusicEnded -> SharedRadioEventWrapper(it, it.musicId)
                        else -> SharedRadioEventWrapper(it, "Unkown".createMusicId())
                    }
                }
                .flatMap { musicid ->
                    logger.info("Request music detail for music ${musicid.value}")

                    eventBus.request<JsonObject>(BusAddresses.MUSIC_DETAIL.address, musicid.value)
                            .map { musicDetailMessage ->
                                val musicDetail = musicDetailMessage.body()
                                EventDto(
                                        musicid.sharedRadioEvent::class.java.simpleName,
                                        musicid.sharedRadioEvent.id.id,
                                        musicDetail
                                )
                            }
                            .map { eventDto ->
                                val res = JsonObject()
                                res.put("eventType", eventDto.eventType)
                                res.put("radioId", eventDto.radioId)
                                res.put("content", eventDto.content)
                                res
                            }
                            .toMulti()
                }


    }

    data class SharedRadioEventWrapper<T>(val sharedRadioEvent: SharedRadioEvent, val value: T)

    @Path("/{sharedRadioId}/detail")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun detail(@PathParam("sharedRadioId") id: String): Uni<JsonObject> =
            eventBus.request<JsonObject>(BusAddresses.SHAREDRADIO_DETAIL.address, SharedRadioId(id))
                    .map {
                        it.body()
                    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun all(): Uni<JsonArray> =
            eventBus.request<JsonArray>(BusAddresses.SHAREDRADIO_LIST.address, "")
                    .map {
                        it.body()
                    }

    data class EventDto(
            val eventType: String,
            val radioId: String,
            val content: JsonObject
    )
}

