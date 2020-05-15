package com.github.jpthiery.hermodr.infra.broadcaster

import arrow.core.Option

class BroadcastPlaylist {

    private val playlist: MutableList<MusicFile> = mutableListOf()

    private val mutex = Object()

    fun addMusic(musicFile: MusicFile) {
        synchronized(mutex) {
            playlist.add(musicFile)
        }
    }

    fun hasNext(): Boolean {
        synchronized(mutex) {
            return playlist.size > 0
        }
    }

    fun next(): Option<MusicFile> {
        synchronized(mutex) {
            return if (playlist.size > 0) {
                val item = playlist.first()
                playlist.remove(item)
                Option.just(item)
            } else {
                Option.empty()
            }
        }
    }

}