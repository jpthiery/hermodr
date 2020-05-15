package com.github.jpthiery.hermodr.infra.musicsource.youtube

import arrow.core.Either
import com.github.jpthiery.hermodr.domain.Music
import com.github.jpthiery.hermodr.domain.MusicScheme
import com.github.jpthiery.hermodr.infra.musicsource.MusicSource
import com.github.kiulian.downloader.YoutubeDownloader
import org.slf4j.LoggerFactory
import ws.schild.jave.AudioAttributes
import ws.schild.jave.Encoder
import ws.schild.jave.EncodingAttributes
import ws.schild.jave.MultimediaObject
import java.io.File
import java.nio.file.Files

class YoutubeMusicSource : MusicSource {

    private val logger = LoggerFactory.getLogger(YoutubeMusicSource::class.java)

    override fun fetch(music: Music, outputFile: File): Either<Exception, Music> {
        val downloader = YoutubeDownloader()
        downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        downloader.setParserRetryOnFailure(1);
        val videoId = music.location
        val video = downloader.getVideo(videoId)
        val formatByItag = video.findFormatByItag(140)
        return formatByItag?.let { format ->
            val outputDir = Files.createTempDirectory("hermodr_$videoId")
            logger.info("Download from Youtube with id: $videoId")
            val downloadFile = video.download(format, outputDir.toFile())
            val title = video.details().title()
            logger.info("Download '$title' from Youtube on file: ${downloadFile.absolutePath}")


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
            encoder.encode(MultimediaObject(downloadFile), outputFile, attrs);
            logger.info("Convert to mp3 file ${outputFile.absolutePath}.")
            Either.right(Music(music.id, MusicScheme.YOUTUBE, outputFile.absolutePath, title))
        } ?: Either.left(RuntimeException("Unable to find mp4 audio format."))
    }

}