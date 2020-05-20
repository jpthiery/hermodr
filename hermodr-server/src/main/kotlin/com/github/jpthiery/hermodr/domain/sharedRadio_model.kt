package com.github.jpthiery.hermodr.domain

import com.github.jpthiery.hermodr.infra.musicsource.MusicSource
import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.Clock
import java.util.*

data class SharedRadioId(val id: String) : StreamId {
    companion object {
        fun fromName(name: String): SharedRadioId = SharedRadioId(
                UUID.nameUUIDFromBytes(name.toByteArray()).toString()
        )
    }
}

sealed class SharedRadioCommand : Command {
    abstract val id: SharedRadioId
    override fun id(): SharedRadioId = id
}

@RegisterForReflection
data class CreateSharedRadio(override val id: SharedRadioId, val name: String, val defaultMusic: Music) : SharedRadioCommand()

@RegisterForReflection
data class AddMusicToSharedRadio(override val id: SharedRadioId, val music: Music) : SharedRadioCommand()

@RegisterForReflection
data class RemovedMusicToSharedRadio(override val id: SharedRadioId, val musicId: MusicId) : SharedRadioCommand()

@RegisterForReflection
data class ValidateMusicToSharedRadio(
        override val id: SharedRadioId,
        val musicId: MusicId,
        val title: String,
        val scheme: MusicScheme,
        val location: String,
        val artist: String = "",
        val album: String = ""
) : SharedRadioCommand()

@RegisterForReflection
data class StartMusicToSharedRadio(override val id: SharedRadioId, val musicId: MusicId) : SharedRadioCommand()

@RegisterForReflection
data class EndMusicToSharedRadio(override val id: SharedRadioId, val musicId: MusicId) : SharedRadioCommand()

sealed class SharedRadioEvent(clock: Clock = Clock.systemUTC()) : Event {
    abstract val id: SharedRadioId
    private val happenedDate: Long = clock.millis()
    override fun id(): SharedRadioId = id
    override fun happenedDate(): Long = happenedDate
}

@RegisterForReflection
data class SharedRadioCreated(override val id: SharedRadioId, val name: String) : SharedRadioEvent()

@RegisterForReflection
data class MusicAdded(override val id: SharedRadioId, val music: Music) : SharedRadioEvent()

@RegisterForReflection
data class MusicRemoved(override val id: SharedRadioId, val musicId: MusicId) : SharedRadioEvent()

@RegisterForReflection
data class MusicValidated(override val id: SharedRadioId, val music: Music) : SharedRadioEvent()

@RegisterForReflection
data class MusicStarted(override val id: SharedRadioId, val musicId: MusicId) : SharedRadioEvent()

@RegisterForReflection
data class MusicFinished(override val id: SharedRadioId, val musicId: MusicId) : SharedRadioEvent()

@RegisterForReflection
data class MusicEnded(override val id: SharedRadioId, val musicId: MusicId) : SharedRadioEvent()

sealed class SharedRadioState : State {
    abstract val id: SharedRadioId
    override fun id(): SharedRadioId = id
}

object SharedRadioNotExit : SharedRadioState() {
    override val id: SharedRadioId
        get() = SharedRadioId("Unknown")
}

data class SharedRadioWaitingMusic(override val id: SharedRadioId, val name: String) : SharedRadioState()
data class SharedRadioBroadcasting(
        override val id: SharedRadioId,
        val name: String,
        val defaultMusic: Music,
        val playlistValidating: List<MusicId> = emptyList(),
        val playlist: List<MusicId> = emptyList()
) : SharedRadioState()
