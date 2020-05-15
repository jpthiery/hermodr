import React, {useState} from "react";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import CardActionArea from "@material-ui/core/CardActionArea";
import Button from "@material-ui/core/Button";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faYoutube} from "@fortawesome/free-brands-svg-icons";
import TextField from "@material-ui/core/TextField";
import {connect} from "react-redux";
import {submitMusicToServer} from "./actions"
import {musicSubmitted, radioSelected} from "../../redux/selectors"
import {makeStyles} from "@material-ui/core/styles";
import {Box} from "@material-ui/core";


export const AddYoutubeMusic = ({radioId, musicsSubmittedToServer, submitMusicToServer}) => {

  const styles = theme => ({
    root: {
      flexGrow: 1,
    },
  });
  const useStyle = makeStyles({
    input: {
      "flex-grow": 10,
    }
  })
  const classes = useStyle();

  const [inputFieldValue, setValue] = useState('')
  const [disableAdd, setDisableAdd] = useState(true)
  if (musicsSubmittedToServer.hasOwnProperty(inputFieldValue)) {
    setValue("")
    setDisableAdd(true)
  }

  function handleAddMusic(event) {
    submitMusicToServer(radioId, {
      source: "YOUTUBE",
      reference: extractReference(inputFieldValue)
    })
  }

  function handleInputChange(event) {
    const value = event.target.value
    setValue(value)
    const reference = extractReference(value)
    setDisableAdd(!/^[0-9A-Za-z_-]{11,}$/.test(reference))
  }

  const extractReference = (inputValue) => {
    try {
      const url = new URL(inputValue)
      if (url.host.startsWith("www.youtube")) {
        return url.searchParams.get("v")
      }
    } catch (e) {

    }
    return inputFieldValue
  }

  return (

    <Card>
      <CardContent>

        <Box display="flex" flexDirection="row">
          <Box display="flex" flexDirection="column">
            <Box>
              &nbsp;
            </Box>
            <Box style={{minWidth: "30px"}}>
              <FontAwesomeIcon icon={faYoutube}/>
            </Box>
            <Box>
              &nbsp;
            </Box>
          </Box>
          <Box flexGrow={1}>
            <TextField
              name="youtube_location"
              variant="outlined"
              size="small"
              classes={classes.input}
              fullWidth={true}
              label="Youtube url video"
              defaultValue="https://www.youtube.com/watch?v=dQw4w9WgXcQ"
              value={inputFieldValue}
              onChange={(e) => handleInputChange(e)}/>
          </Box>
        </Box>

      </CardContent>
      <CardActionArea>
        <Button
          disabled={disableAdd}
          onClick={(event) => handleAddMusic(event)}
        >
          Add to playlist
        </Button>
      </CardActionArea>
    </Card>

  )
}
/*
        <Grid container spacing={4}>
          <Grid item>
            <FontAwesomeIcon icon={faYoutube}/>
          </Grid>
          <Grid item >
            <TextField
              name="youtube_location"
              variant="outlined"
              size="small"
              classes={classes.input}
              fullWidth={true}
              label="Youtube url video"
              value={inputFieldValue}
              onChange={(e) => handleInputChange(e)}/>
          </Grid>
        </Grid>
        */

const mapStateToProps = state => {
  const radioId = radioSelected(state);
  const musicsSubmittedToServer = musicSubmitted(state)
  return {radioId, musicsSubmittedToServer};
};

export default connect(
  mapStateToProps,
  {submitMusicToServer}
)(AddYoutubeMusic)
