import React from 'react';
import ReactDOM from 'react-dom';
import N2O from 'n2o-framework';
import createFactoryConfig from "n2o-framework/lib/core/factory/createFactoryConfig";
import authProvider from "n2o-framework/lib/core/auth/authProvider";

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

ReactDOM.render(<N2O {...createFactoryConfig(config)} />, document.getElementById('n2o'));

declare global {
    interface Window {
        SERVICE_WORKER_PATH: string;
    }
}

if (navigator && navigator.serviceWorker) {
  navigator.serviceWorker.register(window.SERVICE_WORKER_PATH).then(() => {
    console.info('ServiceWorker installing success');
  }, (error) => {
    console.warn('ServiceWorker installing error: ', error);
  });
} else {
  console.warn('ServiceWorker installing error: not supported');
}
