package com.github.jpthiery.hermodr.infra.broadcaster

sealed class MusicFile {
    abstract val title: String
    abstract val filePath: String
}
data class Mp3MusicFile(override val title: String, override val filePath: String): MusicFile()
data class OggMusicFile(override val title: String, override val filePath: String): MusicFile()