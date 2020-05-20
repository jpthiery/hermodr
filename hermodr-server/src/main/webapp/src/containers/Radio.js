import React from "react";
import RadioMetadata from "../components/radio_metadata/RadioMetadata";
import OnAir from "../components/currentPlay/OnAir";
import Playlist from "../components/playlist/Playlist";
import AddMusic from "../components/addMusic/AddMusic";
import {connect} from "react-redux";
import {currentPlay, currentRadio} from "../redux/selectors";
import './radio.css'
import '../App.css'

const Radio = ({radio, musicPlay}) => {

  return (
    <div>
      <div className="radio-metadata-container">
        <RadioMetadata radio={radio}/>
      </div>
      <div className="radio-container">
        <OnAir music={musicPlay}/>
      </div>
      <div className="radio-container">
        <AddMusic/>
      </div>
      <div className="radio-container Green">
        <Playlist/>
      </div>
    </div>
  )
}


const mapStateToProps = state => {
  const radio = currentRadio(state);
  const musicPlay = currentPlay(state);
  return {radio, musicPlay};
};

export default connect(mapStateToProps)(Radio);