import React from "react";

import Card from '@material-ui/core/Card';
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import {makeStyles} from '@material-ui/core/styles';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faYoutube} from '@fortawesome/free-brands-svg-icons'
import {faCheckSquare, faCircleNotch, faDownload} from '@fortawesome/free-solid-svg-icons'

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

  return (
    <Card className={classes.root}>
      <CardContent className={classes.cardContent}>
        <FontAwesomeIcon icon={
          music.source === "YOUTUBE" ? faYoutube : faDownload
        }/>
        <Typography variant="body1" >
          {iconItem} {music.title}
        </Typography>
      </CardContent>
    </Card>
  )

}

export default PlaylistItem

