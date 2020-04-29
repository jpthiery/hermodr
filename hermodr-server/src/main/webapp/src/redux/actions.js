import {
  ADD_MUSIC,
  REQUEST_LIST_RADIOS,
  RESPONSE_LIST_RADIO,
  REQUEST_RADIO_DETAIL,
  RESPONSE_RADIO_DETAIL, VALIDATE_MUSIC, START_MUSIC, END_MUSIC, SUBMIT_MUSIC, MUSIC_SUBMITTED
} from "./actionTypes";


export const requestListRadio = () => ({
  type: REQUEST_LIST_RADIOS,
  payload: {}
})

export const responseListRadio = response => ({
  type: RESPONSE_LIST_RADIO,
  payload: {
    radios: response
  }
})

export const requestRadioDetail = radioId => ({
  type: REQUEST_RADIO_DETAIL,
  payload: {
    radioId: radioId
  }
})

export const responseRadioDetail = response => ({
  type: RESPONSE_RADIO_DETAIL,
  payload: {
    response: response
  }
})

export const submitMusic = music => ({
  type: SUBMIT_MUSIC,
  payload: {
    music: music
  }
})

export const musicSubmitted = music => ({
  type: MUSIC_SUBMITTED,
  payload: {
    music: music
  }
})

export const addMusic = response => ({
  type: ADD_MUSIC,
  payload: {
    music: response
  }
})

export const validateMusic = response => ({
  type: VALIDATE_MUSIC,
  payload: {
    music: response
  }
})

export const startMusic = response => ({
  type: START_MUSIC,
  payload: {
    music: response
  }
})

export const endMusic = response => ({
  type: END_MUSIC,
  payload: {
    music: response
  }
})

