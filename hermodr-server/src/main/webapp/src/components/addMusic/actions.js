import {musicSubmitted, submitMusic} from "../../redux/actions";
import {toast} from 'react-toastify';
import "react-toastify/dist/ReactToastify.css"

export const submitMusicToServer = (radioId, music) => dispatch => {
  dispatch(submitMusic(music))
  fetch(`/sharedRadio/${radioId}/playlist/music`, {
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

}