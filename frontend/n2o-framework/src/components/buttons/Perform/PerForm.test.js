import React from 'react';
import PerformBtn from './Perform';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';

const mockStore = configureMockStore();

const setup = props => {
  const store = mockStore({});
  const wrapper = mount(
    <Provider store={store}>
      <PerformBtn {...props} />
    </Provider>
  );

  return { store, wrapper };
};

describe('<Perform />', () => {
  it('Создание', () => {
    const { wrapper } = setup();
    expect(wrapper.find('Button').exists()).toBeTruthy();
  });
  it('Вызов экшена', async () => {
    const { wrapper, store } = setup({ action: { type: 'n2o/button/Dummy' } });
    await wrapper.find('Button').simulate('click');
    expect(store.getActions()[1]).toEqual({ type: 'n2o/button/Dummy' });
  });
});
