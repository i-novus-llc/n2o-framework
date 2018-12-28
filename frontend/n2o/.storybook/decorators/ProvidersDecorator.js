import React from 'react';
import {addLocaleData, IntlProvider} from 'react-intl';
import ruLocaleData from 'react-intl/locale-data/ru';
import {Provider} from 'react-redux';
import {ConnectedRouter} from 'connected-react-router';
import {createMemoryHistory as createHistory} from 'history'

import FactoryProvider from '../../src/core/factory/FactoryProvider';
import createFactoryConfig from "../../src/core/factory/createFactoryConfig";

import SecurityProvider from '../../src/core/auth/SecurityProvider';
import ModalPages from '../../src/components/core/ModalPages';
import {makeStore} from "./utils";
import authProviderExample from "../auth/authProviderExample";

addLocaleData(ruLocaleData);

const {store, securityConfig, history} = makeStore();

export default (story) => {
  return <IntlProvider locale="ru" messages={{}}>
    <Provider store={store}>
      <SecurityProvider {...securityConfig}>
        <FactoryProvider config={createFactoryConfig({})}>
          <ConnectedRouter history={history}>
            <div>
              {story()}
              <ModalPages/>
            </div>
          </ConnectedRouter>
        </FactoryProvider>
      </SecurityProvider>
    </Provider>
  </IntlProvider>
}