import React from 'react';
import { ModalWindow } from './ModalPage';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import rootReducer from '../../reducers';
import history from '../../history';
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
      <ModalWindow {...props} {...propOverrides} />
    </Provider>
  );
};
describe('Тесты ModalPage', function() {
  it('CoverSpinner не должен рендериться, если metadata не пуста', () => {
    const wrapper = setup({
      pageUrl: '/modalPage',
      pageId: 'modalPage',
      close: false,
      pages: {},
      loading: false,
    });
    setTimeout(() => {
      expect(wrapper.find('.n2o-spinner-container').exists()).toBeFalsy();
    }, 500);
  });
});
