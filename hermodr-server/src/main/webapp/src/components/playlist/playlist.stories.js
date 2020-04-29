import React from 'react';
import Playlist from "./Playlist";
import {createStore} from 'redux'
import {Provider} from "react-redux";
import musicReducer from "../../redux/reducers";
import {addMusic} from "../../redux/actions";

const newStore = () => createStore(
  musicReducer,
  {
    musics: {
      radioSelected: "A",
      radios: {
        "A": {
          currentPLay: {
            id: "A",
            title: "A little song for you",
            source: "Youtube",
            validated: false
          },
          playlist: []
        }
      }
    }
  },
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
)

export default {
  title: 'Playlist'
};

const allMusics = [
  {
    id: "A",
    radioId: "A",
    title: "A little song for you",
    source: "Youtube",
    validated: false
  },
  {
    id: "B",
    radioId: "A",
    title: "A love song",
    source: "User",
    validated: true
  },
  {
    id: "C",
    radioId: "A",
    title: "Hymne national",
    source: "Youtube",
    validated: true
  },
  {
    id: "D",
    radioId: "A",
    title: "My customer song",
    source: "User",
    validated: false
  }
]


export const FullPlaylist = () => {
  let store = newStore()
  allMusics.forEach((music) => store.dispatch(addMusic(music)))
  return <Provider store={store}><Playlist/></Provider>;
}

FullPlaylist.story = {
  name: 'Full Playlist',
};

export const EmptyPlaylist = () => {
  let store = newStore()
  return <Provider store={store}><Playlist/></Provider>;
}

EmptyPlaylist.story = {
  name: 'Empty Playlist',
};
