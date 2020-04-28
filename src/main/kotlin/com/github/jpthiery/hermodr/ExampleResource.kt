package com.github.jpthiery.hermodr

import com.github.jpthiery.hermodr.broadcaster.infra.icecast.BindingLibShout
import org.eclipse.microprofile.config.inject.ConfigProperty
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
        return icecast.libVersion()
    }
}