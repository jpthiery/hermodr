import React from "react";
import Typography from "@material-ui/core/Typography";
import {makeStyles} from '@material-ui/core/styles';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faCheckSquare, faCircleNotch, faDownload} from '@fortawesome/free-solid-svg-icons'
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import {faYoutube} from "@fortawesome/free-brands-svg-icons";

const useStyle = makeStyles({
    root: {
        marginBottom: 20,
        backgroundColor: "#6272A4",
        color: "#FFFFFF"

    },
    cardContent: {
        alignItems: "flex-start"
    }
})

const PlaylistItem = ({music}) => {
    const classes = useStyle();

    let iconItem
    if (music.validated) {
        iconItem = <FontAwesomeIcon icon={faCheckSquare}/>
    } else {
        iconItem = <FontAwesomeIcon icon={faCircleNotch} spin/>
    }

    const renderCaption = (child) => {
        return <Typography variant="caption">
            {child}
        </Typography>
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
        details = renderCaption(text)
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
        <Card className={classes.root}>
            <CardContent className={classes.cardContent}>
                <FontAwesomeIcon icon={
                    music.source === "YOUTUBE" ? faYoutube : faDownload
                }/>
                <Typography variant="body1">
                    {iconItem} {music.title}
                </Typography>
                {details}
                {durationDetail}
            </CardContent>
        </Card>
    )

}

export default PlaylistItem

