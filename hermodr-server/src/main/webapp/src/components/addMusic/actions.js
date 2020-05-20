import {musicSubmitted, submitMusic} from "../../redux/actions";
import "react-toastify/dist/ReactToastify.css"

export const submitMusicToServer = (radioId, music) => dispatch => {
    dispatch(submitMusic(music))
    switch (music.source) {
        case "YOUTUBE":
        fetch
            (`/sharedRadio/${radioId}/playlist/music`, {
                method: 'POST',
                headers: {
                    Accept: 'application/json',
                    "Content-Type": 'application/json'
                },
                body: JSON.stringify(music)
            })
                .then(res => res.json())
                .then(res => {
                    dispatch(musicSubmitted(music))
                })
            break;
        case "LOCALFILE" :
            console.log("Uploading file to server")
            const data = new FormData()
            data.append('music', music.file)
            fetch(`/sharedRadio/${radioId}/playlist/music`, {
                method: 'POST',
                headers: {
                    Accept: 'application/json'
                },
                body: data
            })
                .then(res => res.json())
                .then(res => {
                    dispatch(musicSubmitted(music))
                })
            break;
    }

}