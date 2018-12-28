import React from 'react';
import ReactDOM from 'react-dom';
import 'n2o/dist/n2o.css';
import './demo.css';
import App from './App';
import { unregister } from './registerServiceWorker';

ReactDOM.render(<App />, document.getElementById('n2o'));
unregister();

