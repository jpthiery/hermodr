package com.github.jpthiery.hermodr.infra.broadcaster.icecast

import com.github.jpthiery.hermodr.infra.broadcaster.icecast.binding.BindingLibShout
import java.nio.file.Paths

class BindingLibShoutProvider(
        private val libPath: String,
        private val configuration: IcecastConfiguration
) {

    fun provide(): BindingLibShout {
        val res = BindingLibShout(Paths.get(libPath))
        val mainSettingsResult = res.setHost(configuration.host)
                .then { it.setPort(configuration.port) }
                .then { it.setMount(configuration.mount) }
        if (mainSettingsResult.isSuccess) {

            res.setAgent("Hermodr")
            res.setName("Awersome Hermodr")
            res.setDescription("A shared playlist web radio.")
            res.setGenre("Hard rock ?")
            res.setUrl("http://localhost.fake")

            when(configuration.format) {
                IcecastFormat.MP3 -> res.sendMp3()
                IcecastFormat.OGG -> res.sendOgg()
            }

            when(configuration.protocol) {
                IcecastProtocol.HTTP -> res.useHttp()
                IcecastProtocol.ROAR_AUDIO -> res.useRoarAudio()
                IcecastProtocol.ICY -> res.useIcy()
            }

            if(configuration.user.isNotBlank()) {
                res.setUser(configuration.user)
                res.setPassword(configuration.password)
            }
        }
        return res
    }

}

enum class IcecastFormat {
    MP3,
    OGG
}

enum class IcecastProtocol {
    HTTP,
    ROAR_AUDIO,
    ICY
}

data class IcecastConfiguration(
        val host: String,
        val port: Int,
        val mount: String,
        val format: IcecastFormat = IcecastFormat.MP3,
        val protocol: IcecastProtocol = IcecastProtocol.HTTP,
        val name: String = "",
        val user: String = "",
        val password: String = "",
        val description: String = ""
) {
    init {
        require(host.isNotBlank()) { "host must be defined." }
        require(mount.isNotBlank()) { "mount must be defined." }
        require(port > 0) { "port must be upper than 0." }
    }

    companion object {
        fun newIcecastConfiguration(): (String, Int) -> (String) -> IcecastConfigurationBuilder {
            return { host, port -> { mount -> IcecastConfigurationBuilder(host, port, mount) } }
        }
    }
}

class IcecastConfigurationBuilder(
        private val host: String,
        private val port: Int,
        private val mount: String
) {
    private var protocol: IcecastProtocol = IcecastProtocol.HTTP
    private var format: IcecastFormat = IcecastFormat.MP3
    private var name: String = ""
    private var user: String = ""
    private var password: String = ""
    private var description: String = ""

    fun build() = IcecastConfiguration(
            host,
            port,
            mount,
            format,
            protocol,
            name,
            user, password,
            description
    )

    fun useHttp(): IcecastConfigurationBuilder {
        protocol = IcecastProtocol.HTTP
        return this
    }

    fun useIcy(): IcecastConfigurationBuilder {
        protocol = IcecastProtocol.ICY
        return this
    }

    fun useRoarAudio(): IcecastConfigurationBuilder {
        protocol = IcecastProtocol.ROAR_AUDIO
        return this
    }

    fun sendMp3(): IcecastConfigurationBuilder {
        format = IcecastFormat.MP3
        return this
    }

    fun sendOgg(): IcecastConfigurationBuilder {
        format = IcecastFormat.OGG
        return this
    }

    fun name(name: String): IcecastConfigurationBuilder {
        this.name = name
        return this
    }

    fun description(description: String): IcecastConfigurationBuilder {
        this.description = description
        return this
    }

    fun auth(user: String, password: String): IcecastConfigurationBuilder {
        this.user = user
        this.password = password
        return this
    }

}