/**
 * Created by emamoshin on 05.09.2017.
 */
import React from 'react';

class App extends React.Component {

  render() {
    return (
      <div className="application container-fluid">
        <div className="nav flex-column nav-pills">
          <a class="nav-link" data-toggle="pill" href="#home">Home</a>
          <a class="nav-link" data-toggle="pill" href="#about">About</a>
          <a class="nav-link" data-toggle="pill" href="#table">Table</a>
        </div>
        <div {...this.props} ></div>
      </div>
    );
  }

}

export default App;
