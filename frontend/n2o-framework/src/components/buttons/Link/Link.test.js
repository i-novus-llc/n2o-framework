import React from 'react';
import Link from './Link';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';
import { push } from 'connected-react-router';

const mockStore = configureMockStore();
const delay = timeout => new Promise(res => setTimeout(res, timeout));

const setup = props => {
  const store = mockStore({});
  const wrapper = mount(
    <Provider store={store}>
      <Link {...props} />
    </Provider>
  );

  return { store, wrapper };
};

describe('<Link />', () => {
  it('Создание', () => {
    const { wrapper } = setup({ url: 'testUrl', target: 'blank' });
    expect(wrapper.find('Button').exists()).toBeTruthy();
    expect(wrapper.find('Button').props().tag).toBe('a');
    expect(wrapper.find('Button').props().href).toBe('testUrl');
    expect(wrapper.find('Button').props().target).toBe('blank');
  });
  it('Вызов экшена при клике inner=true', async () => {
    const { wrapper, store } = setup({
      url: 'testUrl',
      target: '_blank',
      inner: true,
      action: { type: 'n2o/button/Dummy' },
    });
    await wrapper.find('Button').simulate('click');
    await delay(100);
    expect(store.getActions()[1]).toEqual(push('testUrl'));
  });
});
