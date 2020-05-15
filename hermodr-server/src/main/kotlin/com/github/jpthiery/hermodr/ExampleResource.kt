package com.github.jpthiery.hermodr

import com.github.jpthiery.hermodr.infra.broadcaster.icecast.BindingLibShoutProvider
import com.github.jpthiery.hermodr.infra.broadcaster.icecast.binding.BindingLibShout
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Path("/hello")
class ExampleResource {

    @Inject
    @field: Default
    lateinit var icecastProvider: BindingLibShoutProvider

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        val icecast = icecastProvider.provide()

        icecast.open()
        icecast.sendSongMetadata("Moby - Spirit")

        val buffer = ByteArray(1024)
        val mp3: InputStream = BufferedInputStream(FileInputStream(File("/home/jpthiery/workspace/perso/hermodr/testB.mp3")))
        var read = mp3.read(buffer)
        while (read > 0) {
            icecast.send(buffer, read)
            read = mp3.read(buffer)
        }

        icecast.close()
        //  return "Successfully configured"

        return icecast.libVersion()
    }
}