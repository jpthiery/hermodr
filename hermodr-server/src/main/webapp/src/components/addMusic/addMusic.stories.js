import React from 'react';

import {AddYoutubeMusic} from "./AddYoutubeMusic";
import {AddMusic} from "./AddMusic";
import {action} from '@storybook/addon-actions';


export default {
    title: 'Add Music'
};

const mockSubmitToServer = (radioId, payloadRef) => {
    action('button-click')
    console.log(payloadRef)
}
export const AddYoutubeMusicStory = () => <AddYoutubeMusic
    radioId="A"
    musicsSubmittedToServer={{"b": "b"}}
    submitMusicToServer={mockSubmitToServer}
/>;

AddYoutubeMusicStory.story = {
    name: 'from Youtube',
};

export const AddMusicStory = () => <AddMusic defaultSelectedSource="local" musicsSubmittedToServer={{}} submitMusicToServer={mockSubmitToServer} />

AddMusicStory.story = {
    name: 'from several source'
}
