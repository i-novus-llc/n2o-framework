import React from 'react';
import { withContext, getContext, compose } from 'recompose';
import PropTypes from 'prop-types';
import { Provider } from 'react-redux';
import { ConnectedRouter } from 'connected-react-router';

import FactoryProvider from '../../src/core/factory/FactoryProvider';
import createFactoryConfig from '../../src/core/factory/createFactoryConfig';

import SecurityProvider from '../../src/core/auth/SecurityProvider';
import OverlayPages from '../../src/components/core/OverlayPages';
import { makeStore } from './utils';

const { store, securityConfig, history } = makeStore();

const OverlayPagesWithContext = compose(
  getContext({ t: PropTypes.func }),
  withContext(
  {
    defaultPromptMessage: PropTypes.string,
  },
  props => ({
    defaultPromptMessage: props.t('defaultPromptMessage'),
  }))
)(OverlayPages);

export default story => {
  return (
    <Provider store={store}>
      <SecurityProvider {...securityConfig}>
        <FactoryProvider config={createFactoryConfig({})}>
          <ConnectedRouter history={history}>
            <div>
              {story()}
              <OverlayPagesWithContext />
            </div>
          </ConnectedRouter>
        </FactoryProvider>
      </SecurityProvider>
    </Provider>
  );
};
