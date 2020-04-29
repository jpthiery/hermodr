package com.github.jpthiery.hermodr.infra.http

import com.github.jpthiery.hermodr.application.BusAddresses
import com.github.jpthiery.hermodr.domain.*
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.core.Vertx
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.*


@Path("/sharedRadio")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class SharedRadioEndpointCommand {

    @Inject
    @field: Default
    lateinit var vertx: Vertx

    @ConfigProperty(name = "radio.defaultMusic.path")
    lateinit var defaultMusicPath: String

    @ConfigProperty(name = "radio.defaultMusic.title")
    lateinit var defaultMusicTitle: String

    @POST
    fun create(requestDto: CreateSharedRadioRequestDto, @Context uriInfo: UriInfo): Uni<Response> {
        val sharedRadioName = requestDto.name
        if (sharedRadioName.isBlank()) {
            return Uni.createFrom().item(
                    Response.status(Response.Status.BAD_REQUEST).build()
            )
        }
        var sharedRadioId = SharedRadioId.fromName(sharedRadioName)
        return vertx.eventBus()
                .request<HandleCommandResult>(BusAddresses.DOMAIN_COMMAND.address,
                        CreateSharedRadio(sharedRadioId, sharedRadioName, Music(
                                "${MusicScheme.LOCALFILE.scheme}/$defaultMusicPath".createMusicId(),
                                MusicScheme.LOCALFILE,
                                defaultMusicPath,
                                defaultMusicTitle
                        ))
                )
                .map {
                    it.body()
                }
                .map {
                    when (it) {
                        is SuccessfullyHandleCommand<*, *> -> {
                            val uriBuilder: UriBuilder = uriInfo.absolutePathBuilder.path("/detail")
                            uriBuilder.path(sharedRadioId.id)
                            Response.accepted(it).location(uriBuilder.build()).build()
                        }
                        is FailedToHandleCommand<*> -> Response.status(Response.Status.BAD_REQUEST).entity(it.reason).build()
                        is NoopToHandleCommand<*> -> Response.status(Response.Status.NOT_MODIFIED).build()
                    }
                }
    }

    @POST
    @Path("/{sharedRadioId}/playlist/music")
    fun addMusic(@PathParam("sharedRadioId") id: String, addMusicRequestDto: AddMusicRequestDto): Uni<Response> {
        val sharedRadioId = SharedRadioId(id)
        return vertx.eventBus()
                .request<HandleCommandResult>(
                        BusAddresses.DOMAIN_COMMAND.address,
                        AddMusicToSharedRadio(
                                sharedRadioId,
                                Music(
                                        "${MusicScheme.YOUTUBE.scheme}/${addMusicRequestDto.reference}".createMusicId(),
                                        MusicScheme.YOUTUBE,
                                        addMusicRequestDto.reference,
                                        "Unknown"
                                )
                        )
                )
                .map {
                    it.body()
                }
                .map {
                    when (it) {
                        is SuccessfullyHandleCommand<*, *> -> {
                            Response.accepted(it).build()
                        }
                        is FailedToHandleCommand<*> -> Response.status(Response.Status.BAD_REQUEST).entity(it.reason).build()
                        is NoopToHandleCommand<*> -> Response.status(Response.Status.NOT_MODIFIED).build()
                    }
                }
    }

}

class CreateSharedRadioRequestDto {
    lateinit var name: String
}

class AddMusicRequestDto {
    lateinit var source: String
    lateinit var reference: String
}
