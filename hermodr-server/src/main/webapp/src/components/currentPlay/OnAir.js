import React from "react";

import Card from '@material-ui/core/Card';
import CardContent from "@material-ui/core/CardContent";
import CardHeader from "@material-ui/core/CardHeader";
import Typography from "@material-ui/core/Typography";
import {makeStyles} from '@material-ui/core/styles';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faYoutube} from '@fortawesome/free-brands-svg-icons'
import {faPlayCircle} from '@fortawesome/free-solid-svg-icons'

const OnAir = ({music}) => {
    const useStyle = makeStyles({
        root: {
            marginBottom: 20,
            backgroundColor: "#53578d",
            color: "#FFFFFF"
        },
        headers: {
            backgroundColor: "#6272A4",
            color: "#FFFFFF",
            fontVariant: "h6"
        },
        musicTitle: {
            color: "#FFFFFF"
        }
    })
    const classes = useStyle();

    let sourceIcon = <FontAwesomeIcon icon={faPlayCircle}/>
    switch (music.source) {
        case "YOUTUBE":
            sourceIcon = <FontAwesomeIcon icon={faYoutube} color="#FF5555"/>
            break
        default:
    }
    let details
    if (music.artist || music.album) {
        let text
        if (music.artist) {
            text = <span>by <b>{music.artist}</b></span>
        }
        if (music.album) {
            text = <span>{text} on album '{music.album}'</span>
        }
        details = <Typography variant="caption" display="block">
            {text}
        </Typography>
    }

    let durationDetail
    if (music.duration > 0) {
        let durationSeconde = music.duration % 60
        let durationMinute = (music.duration - durationSeconde) / 60
        durationDetail = <Typography variant="caption" display="block">
            {durationMinute}:{durationSeconde}
        </Typography>
    }

    return (
        <Card className={classes.root} elevation={3}>
            <CardHeader className={classes.headers} title="On Air"/>
            <CardContent>
                {sourceIcon}
                <Typography variant="body1" className={classes.musicTitle}>
                    <small><u>Titre:</u> </small>{music.title}
                </Typography>
                {details}
                {durationDetail}
            </CardContent>
        </Card>
    )

}

export default OnAir

