package com.github.jpthiery.hermodr.infra.musicsource

import com.github.jpthiery.hermodr.domain.MusicId

interface MusicFileLocator {

    fun locate(musicId: MusicId): String

}