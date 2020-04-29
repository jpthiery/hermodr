package com.github.jpthiery.hermodr.infra.broadcaster

interface Broadcaster {

    fun enqueue(music: MusicFile)

    fun skip()

}