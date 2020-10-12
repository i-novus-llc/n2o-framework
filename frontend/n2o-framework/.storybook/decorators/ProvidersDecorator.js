import React from 'react';
import { withContext, getContext, compose } from 'recompose';
import PropTypes from 'prop-types';
import { Provider } from 'react-redux';
import { ConnectedRouter } from 'connected-react-router';
import { withTranslation } from 'react-i18next';
import '../../src/i18n';

import FactoryProvider from '../../src/core/factory/FactoryProvider';
import createFactoryConfig from '../../src/core/factory/createFactoryConfig';

import SecurityProvider from '../../src/core/auth/SecurityProvider';
import OverlayPages from '../../src/components/core/OverlayPages';
import { makeStore } from './utils';

const { store, securityConfig, history } = makeStore();

const withTranslationComponent = wrapped => compose(
  withTranslation(),
  withContext(
    {
      t: PropTypes.func,
      i18n: PropTypes.func,
    },
    props => ({
      t: props.t,
      i18n: props.i18n,
    }))
)(wrapped);

const OverlayPagesWithContext = compose(
  getContext({ t: PropTypes.func }),
  withContext(
  {
    defaultPromptMessage: PropTypes.string,
  },
  props => ({
    defaultPromptMessage: props.t('defaultPromptMessage'),
  })),
)
(OverlayPages);

export default story => {
  return (
    withTranslationComponent(<Provider store={store}>
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
    </Provider>)
  );
};
