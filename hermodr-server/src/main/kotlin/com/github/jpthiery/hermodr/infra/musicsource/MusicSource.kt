package com.github.jpthiery.hermodr.infra.musicsource

import arrow.core.Either
import com.github.jpthiery.hermodr.domain.Music
import java.io.File

interface MusicSource {

    fun fetch(music: Music, outputFile: File): Either<Exception, Music>

}