import 'react-app-polyfill/ie11';
import 'react-app-polyfill/stable';

import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import { unregister } from './registerServiceWorker';

import 'n2o/dist/n2o.css';
import './demo.css';

ReactDOM.render(<App />, document.getElementById('n2o'));
unregister();

