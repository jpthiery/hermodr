import React, {useEffect} from 'react';
import './App.css';
import store from "./redux/store";
import {fetchListRadio} from "./redux/middleware_actions";
import Radio from "./containers/Radio";
import {radioSelected} from "./redux/selectors";
import {connect} from "react-redux";
import {ToastContainer} from 'react-toastify'

const App = ({radioId}) => {
  useEffect(() => store.dispatch(fetchListRadio()), [])

  const displayRadio = radioId ? <Radio/> : <p>No radio :/</p>

  return (
    <div className="App">
      {displayRadio}

      <ToastContainer
        position="bottom-center"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  );
}
/*
<header className="App-header">
        <img src={logo} className="App-logo" alt="logo"/>
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
 */


const mapStateToProps = state => {
  const radioId = radioSelected(state);
  return {radioId};
};

export default connect(mapStateToProps)(App);
