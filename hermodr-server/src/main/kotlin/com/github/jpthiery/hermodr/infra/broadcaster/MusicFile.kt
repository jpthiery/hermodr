package com.github.jpthiery.hermodr.infra.broadcaster

import com.github.jpthiery.hermodr.domain.MusicId

sealed class MusicFile {
    abstract val id: MusicId
    abstract val title: String
    abstract val filePath: String
}

data class Mp3MusicFile(
        override val id: MusicId,
        override val title: String,
        override val filePath: String
) : MusicFile()

data class OggMusicFile(
        override val id: MusicId,
        override val title: String,
        override val filePath: String
) : MusicFile()