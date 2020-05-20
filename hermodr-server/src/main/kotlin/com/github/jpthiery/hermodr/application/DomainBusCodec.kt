package com.github.jpthiery.hermodr.application

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec

open class DomainBusCodec<T>(
        private val classType: Class<T>
) : MessageCodec<T, T> {

    override fun decodeFromWire(pos: Int, buffer: Buffer?): T {
        val jsonLength = buffer?.getInt(pos + 4) ?: 0
        val json = buffer?.getString(pos + 4, pos + 4 + jsonLength)

        val mapper = ObjectMapper()
        return mapper.readValue(json, classType) as T
    }

    override fun systemCodecID(): Byte = -1

    override fun encodeToWire(buffer: Buffer?, s: T?) {
        val mapper = ObjectMapper()
        val res = mapper.writeValueAsString(s)
        buffer?.appendInt(res.toByteArray().size)
        buffer?.appendString(res)
    }

    override fun transform(s: T?): T = s!!

    override fun name(): String = "${classType.simpleName}Codec"

}