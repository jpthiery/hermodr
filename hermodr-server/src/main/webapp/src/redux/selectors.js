
export const musics = store => store.musics

export const radioSelected = store => musics(store).radioSelected ? musics(store).radioSelected : ""

export const currentRadio = store => musics(store).radios[radioSelected(store)]

export const currentPlaylist = store => currentRadio(store).playlist ? currentRadio(store).playlist : []

export const currentPlay = store => currentRadio(store).hasOwnProperty("currentPlay") ? currentRadio(store).currentPlay : {}

export const musicSubmitted = store => musics(store).musicsSubmitted

