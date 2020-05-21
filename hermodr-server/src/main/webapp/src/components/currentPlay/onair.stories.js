import React from 'react';
import OnAir from "./OnAir";


export default {
    title: 'On Air'
};


export const YoutubeOnAir = () => <OnAir music={{
    id: "C",
    title: "Hymne national",
    source: "YOUTUBE",
    location: "ABCdefg1e",
    validated: true
}}/>

YoutubeOnAir.story = {
    name: 'Youtube music',
};

export const LocalFileOnAir = () => <OnAir music={{
    id: "C",
    title: "Hymne national",
    source: "LOCALFILE",
    location: "ABCdefg1e",
    validated: true
}}/>

LocalFileOnAir.story = {
    name: 'LocalFile music',
};

export const YoutubeOnAirWithMetada = () => <OnAir music={{
    id: "C",
    title: "Hymne national",
    artist: "Myselfe",
    album: "Only for you",
    duration: 223,
    source: "YOUTUBE",
    location: "ABCdefg1e",
    validated: true
}}/>

YoutubeOnAirWithMetada.story = {
    name: 'Youtube music with metadata',
};

export const LocalFileOnAirWithMetada = () => <OnAir music={{
    id: "C",
    title: "Hymne national",
    artist: "Myselfe",
    album: "Only for you",
    duration: 223,
    source: "LOCALFILE",
    location: "ABCdefg1e",
    validated: true
}}/>

LocalFileOnAirWithMetada.story = {
    name: 'LocalFile music with metadata',
};
