import React from 'react';
import { mount } from 'enzyme';
import ButtonsCell from '../ButtonsCell';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';
import sinon from 'sinon';

const mockStore = configureMockStore();
const store = mockStore({});

const setup = (propOverrides = {}) => {
  const props = Object.assign({}, propOverrides);

  const wrapper = mount(
    <Provider store={store}>
      <ButtonsCell {...props} />
    </Provider>
  );

  return {
    props,
    wrapper,
  };
};

describe('<ButtonsCell />', () => {
  it('Проверяет создание Кнопки', () => {
    const { wrapper } = setup({
      toolbar: [
        {
          buttons: [{ title: 'test' }],
        },
      ],
    });

    wrapper.update();
    expect(wrapper.find('Actions').exists()).toBeTruthy();
  });
});
