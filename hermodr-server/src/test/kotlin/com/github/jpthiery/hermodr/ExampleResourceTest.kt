package com.github.jpthiery.hermodr

import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.model.quality.AudioQuality
import com.github.kiulian.downloader.model.quality.VideoQuality
import org.junit.jupiter.api.Test
import ws.schild.jave.AudioAttributes
import ws.schild.jave.Encoder
import ws.schild.jave.EncodingAttributes
import ws.schild.jave.MultimediaObject
import java.io.File


//@QuarkusTest
class ExampleResourceTest {
    /*
        @Test
        fun testHelloEndpoint() {
            given()
                    .`when`().get("/hello")
                    .then()
                    .statusCode(200)
                    .body(`is`("hello"))
        }
    */
    //@Test
    fun `youtube downloader test`() {
        // init downloader
        var downloader = YoutubeDownloader();

// extractor features
        downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        downloader.setParserRetryOnFailure(1);

// parsing data
        val videoId = "mrNmUKjsaKI"; // for url https://www.youtube.com/watch?v=abc12345
        val video = downloader.getVideo(videoId);

// video details
        val details = video.details()
        println(details.title())
        println(details.author())
        println(details.viewCount())

// get videos with audio

// itags can be found here - https://gist.github.com/sidneys/7095afe4da4ae58694d128b1034e01e2
        val audioFormats = video.findAudioWithQuality(AudioQuality.unknown)
        video.formats().forEach { println("${it.itag()} : ${it.mimeType()} ${it.type()} ") }
        val formatByItag = video.findFormatByItag(140)
        if (formatByItag != null) {
            System.out.println(formatByItag.url());
        }

        val outputDir = File("my_videos");
/*
// sync downloading
        val file = video.download(formatByItag, outputDir);

        val target = File("output.mp3");

                //Audio Attributes
        val audio =  AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        //Encoding attributes
        val  attrs = EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);

        //Encode
        val encoder = Encoder();
        encoder.encode(MultimediaObject(file), target, attrs);
*/
    }
}