import React from "react";
import PlaylistItem from "./PlaylistItem";
import {currentPlaylist} from "../../redux/selectors";
import {connect} from "react-redux";
import Typography from "@material-ui/core/Typography";
import {makeStyles} from "@material-ui/core/styles";


const useStyle = makeStyles({
  root: {
    alignItems: "flex-start"
  }
})

const Playlist = ({playlist}) => {
  const classes = useStyle();
  return (
    <div className={classes.root}>
      {playlist && playlist.length ?
        playlist.map((music, index) => {
            return <PlaylistItem key={music.id} music={music}/>;
          }
        ) : <Typography variant="h3" component="h4">No song.</Typography>
      }
    </div>
  )
}

const mapStateToProps = state => {
  const playlist = currentPlaylist(state);
  return {playlist};
};

export default connect(mapStateToProps)(Playlist);