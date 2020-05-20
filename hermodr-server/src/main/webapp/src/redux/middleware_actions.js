import {
    addMusic,
    removeMusic,
    requestListRadio,
    requestRadioDetail,
    responseListRadio,
    responseRadioDetail,
    startMusic,
    validateMusic
} from './actions'
import {toast} from "react-toastify";

function fetchListRadio() {
    return dispatch => {
        dispatch(requestListRadio())
        fetch("/sharedRadio/query/all", {
            method: 'GET',
            headers: {
                Accept: 'application/json',
            }
        })
            .then(response => {
                response.json()
                    .then(json => {
                        dispatch(responseListRadio(json))
                        if (json && json.length > 0) dispatch(fetchRadioDetail(json[0]['id']))
                    });
            })
    }
}

function fetchRadioDetail(radioId) {
    return dispatch => {
        dispatch(requestRadioDetail(radioId))
        fetch(`/sharedRadio/query/${radioId}/detail`, {
            method: 'GET',
            headers: {
                Accept: 'application/json',
            }
        })
            .then(res => res.json())
            .then(res => {
                console.log(res)
                dispatch(responseRadioDetail(res))

                const source = new EventSource(`/sharedRadio/query/${radioId}/events`);
                source.onmessage = function (event) {
                    const eventData = JSON.parse(event.data)
                    if (eventData['eventType'] && eventData['radioId'] && eventData['content']) {
                        const music = {
                            radioId: eventData['radioId'],
                            ...eventData['content']
                        }
                        const eventType = eventData['eventType']
                        switch (eventType) {
                            case "MusicAdded":
                                if(music.source === "YOUTUBE") {
                                    toast.info(`Your youtube Video Id ${music.location} will be added`, {
                                        position: "bottom-center",
                                        type: "info",
                                        autoClose: 5000
                                    });
                                }
                                dispatch(addMusic(music))
                                break
                            case "MusicValidated":
                                dispatch(validateMusic(music))
                                break
                            case "MusicStarted":
                                dispatch(startMusic(music))
                                break
                            case "MusicRefused":
                                dispatch(removeMusic(music))
                                break
                            default:
                        }
                    }
                };
                return res
            })
    }
}

export {fetchListRadio, fetchRadioDetail}