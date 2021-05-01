import React from 'react';
import { ModalWindow } from './ModalPage';
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
    pageUrl: '/modalPage',
    pageId: 'modalPage',
    visible: true,
    close: false,
    pages: {},
  };
  return mount(
    <Provider store={{ ...store, ...storeOverrides }}>
      <FactoryProvider config={createFactoryConfig({})}>
        <ModalWindow {...props} {...propOverrides} />
      </FactoryProvider>
    </Provider>
  );
};
describe.skip('Тесты ModalPage', function() {
  it('CoverSpinner не должен рендериться, если metadata не пуста', () => {
    const wrapper = setup({
      pageUrl: '/modalPage',
      pageId: 'modalPage',
      close: false,
      pages: {},
      loading: false,
    });

    expect(wrapper.find('.n2o-spinner-container').exists()).toBeFalsy();
  });
});

it('Отображается ли окно', () => {
  const wrapper = setup({
    visible: true,
  });
  expect(wrapper.find('.modal-content').length).toBe(1);
});

it('Отображается ли hasHeader', () => {
  const wrapper = setup({
    visible: true,
    hasHeader: true,
  });

  expect(wrapper.find('.modal-title').length).toBe(1);
});
