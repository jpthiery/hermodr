package com.github.jpthiery.hermodr.infra.musicsource.local

import arrow.core.Either
import com.github.jpthiery.hermodr.domain.Music
import com.github.jpthiery.hermodr.infra.musicsource.AbstractMp3fileRepository
import java.io.File

class LocalFileMusicSource : AbstractMp3fileRepository() {

    override fun fetch(music: Music, outputFile: File): Either<Exception, Music> {
        val inputFile = File(music.location)
        return convertToMp3AndMove(music, inputFile, outputFile)
    }
}