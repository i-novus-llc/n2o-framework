import React from 'react';
import mockStore from 'redux-mock-store';
import { IntlProvider } from 'react-intl';
import { Provider } from 'react-redux';
import Factory from '../../../core/factory/Factory';
import FactoryProvider from '../../../core/factory/FactoryProvider';
import createFactoryConfig from '../../../core/factory/createFactoryConfig';
import ListWidgetMeta from './List.meta';
import { createStore } from 'redux';
import reducers from '../../../reducers';

const store = createStore(reducers);

const setup = propsOverride => {
  return mount(
    <Provider store={store}>
      <FactoryProvider config={createFactoryConfig({})}>
        <Factory {...ListWidgetMeta['List']} {...propsOverride} />
      </FactoryProvider>
    </Provider>
  );
};

describe('Проверка ListContainer', () => {
  it('Маппит данные в компоненты', () => {
    // const wrapper = setup();
  });
});
