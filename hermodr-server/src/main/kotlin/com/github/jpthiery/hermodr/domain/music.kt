package com.github.jpthiery.hermodr.domain

import com.github.jpthiery.hermodr.infra.broadcaster.MusicFile
import java.util.*


typealias MusicId = String

fun String.createMusicId(): MusicId = UUID.nameUUIDFromBytes(this.toByteArray()).toString()
fun MusicFile.createMusicId(): MusicId = UUID.nameUUIDFromBytes(this.title.toByteArray()).toString()

enum class MusicScheme(
        val scheme: String
) {
    LOCALFILE("file://"),
    YOUTUBE("youtube://")
}

data class Music(
        val id: MusicId,
        val scheme: MusicScheme,
        val location: String,
        val title: String,
        val artist: String = "",
        val album: String = "",
        val duration: Long = -1
)
