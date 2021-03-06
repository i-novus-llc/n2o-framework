import React from 'react';
import { Provider } from 'react-redux';
import { ConnectedRouter } from 'connected-react-router';

import { makeStore } from './utils';

import FactoryProvider from '../../src/core/factory/FactoryProvider';
import createFactoryConfig from '../../src/core/factory/createFactoryConfig';
import SecurityProvider from '../../src/core/auth/SecurityProvider';
import OverlayPages from '../../src/components/core/OverlayPages';
import "../i18n";

const { store, securityConfig, history } = makeStore();

export default story => {
  return (
    <Provider store={store}>
      <SecurityProvider {...securityConfig}>
        <FactoryProvider config={createFactoryConfig({})}>
          <ConnectedRouter history={history}>
            <div>
              {story()}
              <OverlayPages />
            </div>
          </ConnectedRouter>
        </FactoryProvider>
      </SecurityProvider>
    </Provider>
  );
};
