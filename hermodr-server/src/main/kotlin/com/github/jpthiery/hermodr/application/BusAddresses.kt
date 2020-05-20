package com.github.jpthiery.hermodr.application

enum class BusAddresses(val address: String) {

    DOMAIN_COMMAND("domain.command"),
    DOMAIN_EVENT("domain.event"),
    APPLICATION_ERROR("application.error"),
    SHAREDRADIO_LIST("application.sharedradio.list"),
    SHAREDRADIO_DETAIL("application.sharedradio.detail"),
    MUSIC_DETAIL("application.music.detail"),
    MUSIC_UPLOADER("application.music.uploader")

}