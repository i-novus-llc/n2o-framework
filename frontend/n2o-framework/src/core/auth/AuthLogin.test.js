import React from 'react';
import AuthLogin from './AuthLogin';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import reducers from '../../reducers';
import history from '../../history';

const setup = (store, props) => {
  return mount(
    <Provider store={store}>
      <AuthLogin {...props} />
    </Provider>
  );
};

describe('Проверка AuthLogin', () => {
  it('компонент отрисовывается', () => {
    const store = createStore(reducers(history));
    const wrapper = setup(store, {
      permissions: true,
      user: {
        username: 'testName',
      },
    });
    expect(
      wrapper
        .find('Login')
        .find('div')
        .exists()
    ).toEqual(true);
  });

  it('handleChange срабатывает', () => {
    const store = createStore(reducers(history));
    const wrapper = setup(store, {
      permissions: true,
      user: {
        username: 'testName',
      },
    });
    wrapper.find('input').simulate('change', {
      target: {
        value: 'userName',
      },
    });
    expect(wrapper.find('Login').state().username).toEqual('userName');
  });
});
