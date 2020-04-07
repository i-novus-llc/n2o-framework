import React from 'react';
import { DrawerWindow } from './DrawerPage';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import rootReducer from '../../reducers';
import history from '../../history';
import FactoryProvider from '../../core/factory/FactoryProvider';
import createFactoryConfig from '../../core/factory/createFactoryConfig';

function configureStore() {
  return createStore(rootReducer(history));
}
const store = configureStore();
const setup = (propOverrides, storeOverrides) => {
  const props = {
    pageUrl: '/drawerPage',
    pageId: 'drawerPage',
    visible: true,
    close: false,
    pages: {},
  };
  return mount(
    <Provider store={{ ...store, ...storeOverrides }}>
      <FactoryProvider config={createFactoryConfig({})}>
        <DrawerWindow {...props} {...propOverrides} />
      </FactoryProvider>
    </Provider>
  );
};
describe('Тесты DrawerPage', function() {
  it('CoverSpinner не должен рендериться, если metadata не пуста', () => {
    const wrapper = setup({
      pageUrl: '/drawerPage',
      pageId: 'drawerPage',
      close: false,
      pages: {},
      loading: false,
    });
    setTimeout(() => {
      expect(wrapper.find('.n2o-spinner-container').exists()).toBeFalsy();
    }, 500);
  });
});
