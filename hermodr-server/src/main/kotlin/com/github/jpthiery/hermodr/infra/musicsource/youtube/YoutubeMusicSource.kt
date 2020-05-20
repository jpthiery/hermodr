package com.github.jpthiery.hermodr.infra.musicsource.youtube

import arrow.core.Either
import com.github.jpthiery.hermodr.domain.Music
import com.github.jpthiery.hermodr.infra.musicsource.AbstractMp3fileRepository
import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.YoutubeException
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files

class YoutubeMusicSource : AbstractMp3fileRepository() {

    private val logger = LoggerFactory.getLogger(YoutubeMusicSource::class.java)

    override fun fetch(music: Music, outputFile: File): Either<Exception, Music> {
        val downloader = YoutubeDownloader()
        downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        downloader.setParserRetryOnFailure(1);
        val videoId = music.location
        return try {
            val video = downloader.getVideo(videoId)
            val formatByItag = video.findFormatByItag(140)
            formatByItag?.let { format ->
                val outputDir = Files.createTempDirectory("hermodr_$videoId")
                logger.info("Download from Youtube with id: $videoId")
                val downloadFile = video.download(format, outputDir.toFile())
                val title = video.details().title()
                logger.info("Download '$title' from Youtube on file: ${downloadFile.absolutePath}")

                convertToMp3AndMove(music.copy(title = title), downloadFile, outputFile)
            } ?: Either.left(RuntimeException("Unable to find mp4 audio format."))
        } catch (e: Exception) {
            Either.left(RuntimeException("Unable to processing youtube video id $videoId", e))
        }
    }

}