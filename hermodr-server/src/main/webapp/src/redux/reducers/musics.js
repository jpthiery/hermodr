import {
  ADD_MUSIC,
  MUSIC_SUBMITTED,
  RESPONSE_LIST_RADIO,
  RESPONSE_RADIO_DETAIL,
  START_MUSIC,
  VALIDATE_MUSIC
} from "../actionTypes";

const initialState = {
  radios: {},
  radioSelected: "",
  musicsSubmitted: {}
};

export default function (state = initialState, action) {
  switch (action.type) {
    case MUSIC_SUBMITTED: {
      const {music} = action.payload;
      return {
        ...state,
        musicsSubmitted: {
          ...state.musicsSubmitted,
          [music.reference]: music.source
        }
      }
    }
    case ADD_MUSIC: {
      const {music} = action.payload;
      return {
        ...state,
        radios: {
          ...state.radios,
          [music.radioId]: {
            ...state.radios[music.radioId],
            playlist: [
              ...state.radios[music.radioId].playlist,
              music
            ]
          }
        }
      };
    }
    case VALIDATE_MUSIC: {
      const {music} = action.payload;
      const oldPlaylist = state.radios[music.radioId].playlist
      const newPlaylist = []
      oldPlaylist.forEach(value => {
        if (value.id !== music.id) {
          newPlaylist.push(value)
        }
      })
      newPlaylist.push(music)
      return {
        ...state,
        radios: {
          ...state.radios,
          [music.radioId]: {
            ...state.radios[music.radioId],
            playlist: newPlaylist
          }
        }
      };
    }
    case START_MUSIC: {
      const {music} = action.payload;
      const oldPlaylist = state.radios[music.radioId].playlist
      const newPlaylist = []
      oldPlaylist.forEach(value => {
        if (value.id !== music.id) {
          newPlaylist.push(value)
        }
      })
      return {
        ...state,
        radios: {
          ...state.radios,
          [music.radioId]: {
            ...state.radios[music.radioId],
            playlist: newPlaylist,
            currentPlay: music
          },
        }
      }
    }
    case RESPONSE_LIST_RADIO: {
      const {radios} = action.payload
      const newRadios = {}
      radios.forEach(radio => newRadios[radio.id] = radio)
      return {
        ...state,
        radios: {
          ...state.radios,
          ...newRadios
        },
        radioSelected: radios[0].id
      }
    }
    case RESPONSE_RADIO_DETAIL: {
      const {response} = action.payload
      const radioId = response.id

      return {
        ...state,
        radioSelected: response.id,
        radios: {
          ...state.radios,
          [radioId]: response
        }
      }
    }
    default:
      return state;
  }
}

