package com.github.jpthiery.hermodr

import com.github.jpthiery.hermodr.broadcaster.infra.icecast.BindingLibShout
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Paths
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Path("/hello")
class ExampleResource {

    @ConfigProperty(name = "libPath")
    lateinit var libPath: String

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        var icecast = BindingLibShout(Paths.get(libPath))
        var initResult = icecast.setHost("icecast.jpthiery.techx.fr")
                .then { it.setPort(8000) }
                .then { it.useHttp() }
                .then { it.setUser("source") }
                .then { it.setPassword("sourcejpthiery") }
                .then { it.sendMp3() }
                .then { it.setMount("test") }
        if (initResult.isSuccess) {
            icecast.open()
            val buffer = ByteArray(1024)
            val mp3: InputStream = BufferedInputStream(FileInputStream(File("/Users/jpthiery/workspace/broadcaster/test.mp3")))
            var read = mp3.read(buffer)
            while (read > 0) {
                icecast.send(buffer, read)
                read = mp3.read(buffer)
            }

            icecast.close()
            return "Successfully configured"
        }
        return icecast.libVersion()
    }
}