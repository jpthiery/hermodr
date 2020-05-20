package com.github.jpthiery.hermodr.infra.musicsource

import arrow.core.Either
import com.github.jpthiery.hermodr.domain.Music
import com.mpatric.mp3agic.Mp3File
import org.slf4j.LoggerFactory
import ws.schild.jave.AudioAttributes
import ws.schild.jave.Encoder
import ws.schild.jave.EncodingAttributes
import ws.schild.jave.MultimediaObject
import java.io.File
import java.nio.file.Files

abstract class AbstractMp3fileRepository : MusicSource {


    private val logger = LoggerFactory.getLogger(AbstractMp3fileRepository::class.java)

    protected fun convertToMp3AndMove(music: Music, inputFile: File, destination: File): Either<Exception, Music> {
        if (!inputFile.exists()) {
            return Either.left(RuntimeException("Music file ${inputFile.absolutePath} not exist, unable to move to music repository."))
        }

        if (isMp3(inputFile)) {
            if (inputFile.renameTo(destination)) {
                val mp3Tag = Mp3File(destination)
                val res = when {
                    mp3Tag.hasId3v1Tag() -> {
                        val id3Tag = mp3Tag.id3v1Tag
                        music.copy(
                                title = id3Tag.title,
                                location = destination.absolutePath,
                                album = id3Tag.album,
                                artist = id3Tag.artist,
                                duration = mp3Tag.lengthInSeconds
                        )
                    }
                    mp3Tag.hasId3v2Tag() -> {
                        val id3Tag = mp3Tag.id3v2Tag
                        music.copy(
                                title = id3Tag.title,
                                location = destination.absolutePath,
                                album = id3Tag.album,
                                artist = id3Tag.artist,
                                duration = mp3Tag.lengthInSeconds
                        )
                    }
                    else -> {
                        music
                    }
                }
                inputFile.delete()
                return Either.right(res)
            } else {
                return Either.left(RuntimeException("Unable to move MP3 file ${inputFile.absolutePath} to ${destination.absolutePath}."))
            }
        } else {
            //Audio Attributes
            val audio = AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            //Encoding attributes
            val attrs = EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);

            //Encode
            val encoder = Encoder();
            encoder.encode(MultimediaObject(inputFile), destination, attrs);
            logger.debug("Convert to mp3 file ${destination.absolutePath}.")
            val mp3Tag = Mp3File(destination)
            return Either.right(
                    music.copy(
                            location = destination.absolutePath,
                            duration = mp3Tag.lengthInSeconds
                    )
            )
        }

    }

    private fun isMp3(file: File): Boolean {
        val path = file.toPath()
        val mimeType = Files.probeContentType(path)
        logger.debug("File ${file.absolutePath} minetype = $mimeType")
        return mimeType == "audio/mpeg"
    }

}