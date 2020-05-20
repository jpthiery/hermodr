package com.github.jpthiery.hermodr.infra.http

import arrow.core.Option
import arrow.core.getOrElse
import com.github.jpthiery.hermodr.application.BusAddresses
import com.github.jpthiery.hermodr.domain.*
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.core.Vertx
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.plugins.providers.multipart.InputPart
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo


@Path("/sharedRadio")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class SharedRadioEndpointCommand {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Inject
    @field: Default
    lateinit var vertx: Vertx

    @ConfigProperty(name = "radio.defaultMusic.path")
    lateinit var defaultMusicPath: String

    @ConfigProperty(name = "radio.defaultMusic.title")
    lateinit var defaultMusicTitle: String

    @ConfigProperty(name = "radio.defaultMusic.upload.dir")
    lateinit var musicUploadedDirectory: String

    @POST
    fun create(requestDto: CreateSharedRadioRequestDto, @Context uriInfo: UriInfo): Uni<Response> {
        val sharedRadioName = requestDto.name
        if (sharedRadioName.isBlank()) {
            return Uni.createFrom().item(
                    Response.status(Response.Status.BAD_REQUEST).build()
            )
        }
        val sharedRadioId = SharedRadioId.fromName(sharedRadioName)
        return executeCommand(
                CreateSharedRadio(
                        sharedRadioId,
                        sharedRadioName,
                        Music(
                                "${MusicScheme.LOCALFILE.scheme}/$defaultMusicPath".createMusicId(),
                                MusicScheme.LOCALFILE,
                                defaultMusicPath,
                                defaultMusicTitle
                        )
                )
        )
    }

    @POST
    @Path("/{sharedRadioId}/playlist/music")
    fun addMusic(@PathParam("sharedRadioId") id: String, addMusicRequestDto: AddMusicRequestDto): Uni<Response> {
        val sharedRadioId = SharedRadioId(id)
        return executeCommand(
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
    }

    private fun executeCommand(command: SharedRadioCommand): Uni<Response> =
            vertx.eventBus()
                    .request<HandleCommandResult>(
                            BusAddresses.DOMAIN_COMMAND.address,
                            command
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

    @POST
    @Path("/{sharedRadioId}/playlist/music")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun addMusic(@PathParam("sharedRadioId") id: String, multiPart: MultipartFormDataInput): Uni<Response> {

        val sharedRadioId = SharedRadioId(id)
        val uploadForm = multiPart.formDataMap
        val inputPart = uploadForm[uploadForm.keys.first()]
        return inputPart?.map { part ->
            extractFileName(part)
                    .map { fileName ->
                        val filePath = writeMusicUploaded(fileName, part)
                        executeCommand(
                                AddMusicToSharedRadio(
                                        sharedRadioId,
                                        Music(
                                                "${MusicScheme.LOCALFILE.scheme}/${filePath}".createMusicId(),
                                                MusicScheme.LOCALFILE,
                                                filePath,
                                                fileName
                                        )
                                )
                        )
                    }
        }
                ?.first()
                ?.getOrElse {
                    Uni.createFrom().item(Response.noContent().build())
                } ?: Uni.createFrom().item(Response.noContent().build())

    }

    private fun writeMusicUploaded(fileName: String, part: InputPart): String {
        val outputFile = File(File(musicUploadedDirectory), fileName)
        if (!outputFile.exists()) outputFile.createNewFile()
        val output = FileOutputStream(outputFile)
        val buffer = ByteArray(1024)
        val input = part.getBody(InputStream::class.java, null)
        var read = input.read(buffer)
        while (read > 0) {
            output.write(buffer, 0, read)
            read = input.read(buffer)
        }
        output.flush()
        output.close()
        input.close()
        return outputFile.absolutePath
    }
    
    private fun extractFileName(inputPart: InputPart): Option<String> =
            inputPart.headers.getFirst("Content-Disposition")?.let {
                val disposition = it
                        .split(";")
                        .first { fileName ->
                            fileName.trim().toLowerCase().startsWith("filename")
                        }

                Option.fromNullable(disposition.split("=")[1].replace("\"", ""))
            } ?: Option.empty()
}


class CreateSharedRadioRequestDto {
    lateinit var name: String
}

class AddMusicRequestDto {
    lateinit var source: String
    lateinit var reference: String
}
