import React from 'react';
import { withContext } from 'recompose';
import PropTypes from 'prop-types';
import {addLocaleData, IntlProvider} from 'react-intl';
import ruLocaleData from 'react-intl/locale-data/ru';
import {Provider} from 'react-redux';
import {ConnectedRouter} from 'connected-react-router';

import FactoryProvider from '../../src/core/factory/FactoryProvider';
import createFactoryConfig from "../../src/core/factory/createFactoryConfig";

import SecurityProvider from '../../src/core/auth/SecurityProvider';
import ModalPages from '../../src/components/core/ModalPages';
import {makeStore} from "./utils";

addLocaleData(ruLocaleData);

const {store, securityConfig, history} = makeStore();

const ModalPagesWithContext = withContext(
  {
    defaultPromptMessage: PropTypes.string
  },
  props => ({
    defaultPromptMessage: 'Все несохраненные данные будут утеряны, вы уверены, что хотите уйти?',
  })
)(ModalPages);

export default (story) => {
  return <IntlProvider locale="ru" messages={{}}>
    <Provider store={store}>
      <SecurityProvider {...securityConfig}>
        <FactoryProvider config={createFactoryConfig({})}>
          <ConnectedRouter history={history}>
            <div>
              {story()}
              <ModalPagesWithContext/>
            </div>
          </ConnectedRouter>
        </FactoryProvider>
      </SecurityProvider>
    </Provider>
  </IntlProvider>
}