import React, {Component} from 'react';
import PropTypes from 'prop-types';
import N2o from 'n2o';

import TestPage from './pages/TestPage';

class App extends Component {
  constructor(props) {
    super(props);
  }

  generateProjectRoutes() {
    return [
      {
        path: '/TestPage/manual',
        component: TestPage,
      }
    ]
  }


  render() {
    return (
      <N2o routes={this.generateProjectRoutes()} />
    );
  }
}

export default App;