package com.github.jpthiery.hermodr.domain

import arrow.core.Either

class SharedRadio : Aggregate<SharedRadioCommand, SharedRadioState, SharedRadioEvent> {

    override fun decide(command: SharedRadioCommand, state: SharedRadioState): Either<String, List<SharedRadioEvent>> =
            when (state) {
                is SharedRadioNotExit -> decideOnNotExist(command)
                is SharedRadioWaitingMusic -> decideOnWaitingMusic(command, state)
                is SharedRadioBroadcasting -> decideOnBroadcasting(command, state)
            }

    override fun apply(state: SharedRadioState, event: SharedRadioEvent): SharedRadioState =
            when (state) {
                is SharedRadioNotExit -> applyOnNotExist(event)
                is SharedRadioWaitingMusic -> applyOnWaitingMusic(state, event)
                is SharedRadioBroadcasting -> applyOnWBroadcasting(state, event)
            }

    override fun notExistState(): SharedRadioState = SharedRadioNotExit

    override fun getEventType(): Class<SharedRadioEvent> = SharedRadioEvent::class.java

    private fun decideOnNotExist(command: SharedRadioCommand): Either<String, List<SharedRadioEvent>> =
            when (command) {
                is CreateSharedRadio -> Either.right(
                        listOf(
                                SharedRadioCreated(command.id, command.name),
                                MusicAdded(command.id, Music(
                                        command.defaultMusic.id,
                                        MusicScheme.LOCALFILE,
                                        command.defaultMusic.location,
                                        command.defaultMusic.title
                                )),
                                MusicValidated(
                                        command.id,
                                        command.defaultMusic
                                )
                        )
                )
                else -> Either.right(emptyList())
            }

    private fun decideOnWaitingMusic(command: SharedRadioCommand, state: SharedRadioWaitingMusic): Either<String, List<SharedRadioEvent>> =
            when (command) {
                is CreateSharedRadio -> Either.left("Shared radio ${command.name} already exist.")
                is AddMusicToSharedRadio -> Either.right(
                        listOf(
                                MusicAdded(state.id, command.music)
                        )
                )
                is ValidateMusicToSharedRadio -> Either.right(
                        listOf(
                                MusicValidated(state.id, Music(command.musicId, command.scheme, command.location, command.title))
                        )
                )
                is StartMusicToSharedRadio -> Either.right(
                        listOf(
                                MusicStarted(state.id, command.musicId)
                        )
                )
                is EndMusicToSharedRadio -> Either.right(
                        listOf(
                                MusicFinished(state.id, command.musicId),
                                MusicEnded(state.id, command.musicId)
                        )
                )
            }

    private fun decideOnBroadcasting(command: SharedRadioCommand, state: SharedRadioBroadcasting): Either<String, List<SharedRadioEvent>> =
            when (command) {
                is CreateSharedRadio -> Either.left("Shared radio ${command.name} already exist.")
                is AddMusicToSharedRadio -> Either.right(
                        listOf(
                                MusicAdded(state.id, command.music)
                        )
                )
                is ValidateMusicToSharedRadio -> Either.right(
                        listOf(
                                MusicValidated(state.id, Music(command.musicId, command.scheme, command.location, command.title))
                        )
                )
                is StartMusicToSharedRadio -> Either.right(
                        listOf(
                                MusicStarted(state.id, command.musicId)
                        )
                )
                is EndMusicToSharedRadio -> Either.right(
                        listOf(
                                MusicFinished(state.id, command.musicId),
                                MusicEnded(state.id, command.musicId)
                        )
                )
            }

    private fun applyOnNotExist(event: SharedRadioEvent) =
            when (event) {
                is SharedRadioCreated -> SharedRadioWaitingMusic(
                        event.id,
                        event.name
                )
                else -> SharedRadioNotExit
            }

    private fun applyOnWaitingMusic(state: SharedRadioWaitingMusic, event: SharedRadioEvent) =
            when (event) {
                is MusicAdded -> SharedRadioBroadcasting(
                        state.id,
                        state.name,
                        event.music,
                        listOf(event.music.id)
                )
                is MusicValidated -> SharedRadioBroadcasting(
                        state.id,
                        state.name,
                        event.music,
                        emptyList(),
                        listOf(event.music.id)
                )
                else -> state
            }

    private fun applyOnWBroadcasting(state: SharedRadioBroadcasting, event: SharedRadioEvent) =
            when (event) {
                is MusicAdded -> {
                    val newPlaylistValidating = state.playlistValidating.toMutableList()
                    newPlaylistValidating.add(event.music.id)
                    state.copy(playlistValidating = newPlaylistValidating.toList())
                }
                is MusicValidated -> {
                    val newPlaylistValidating = state.playlistValidating.toMutableList()
                    if (state.playlistValidating.contains(event.music.id)) {
                        newPlaylistValidating.remove(event.music.id)
                    }
                    val newPlaylist = state.playlist.toMutableList()
                    newPlaylist.add(event.music.id)
                    state.copy(
                            playlistValidating = newPlaylistValidating.toList(),
                            playlist = newPlaylist.toList()
                    )
                }
                else -> state
            }
}