import React from 'react';
import ReactDOM from 'react-dom';
import N2O from 'n2o-framework';
import { authProvider } from 'n2o-auth';

import 'n2o-framework/dist/n2o.css';
import './index.css';

const config = {
  security: {
    authProvider,
    externalLoginUrl: '/'
  },
  messages: {
    timeout: {
      error: 0,
      success: 5000,
      warning: 0,
      info: 0,
    }
  }
};

ReactDOM.render(<N2O {...config} />, document.getElementById('n2o'));