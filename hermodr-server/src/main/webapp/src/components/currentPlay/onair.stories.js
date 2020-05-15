import React from 'react';
import OnAir from "./OnAir";


export default {
  title: 'On Air'
};

const allMusics = [
  {
    id: "A",
    title: "A little song for you",
    source: "Youtube",
    validated: false
  },
  {
    id: "B",
    title: "A love song",
    source: "User",
    validated: true
  },
  {
    id: "C",
    title: "Hymne national",
    source: "Youtube",
    location: "ABCdefg1e",
    validated: true
  },
  {
    id: "D",
    title: "My customer song",
    source: "User",
    validated: false
  }
]


export const YoutubeOnAir = () => <OnAir music={{
  id: "C",
  title: "Hymne national",
  source: "YOUTUBE",
  location: "ABCdefg1e",
  validated: true
}} />

YoutubeOnAir.story = {
  name: 'Youtube music',
};
