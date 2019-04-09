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
      buttons: [{ title: 'test' }],
    });

    wrapper.update();
    expect(wrapper.find('HintButton').exists()).toBeTruthy();
  });

  it('Проверяет создание Dropdown', () => {
    const { wrapper } = setup({
      buttons: [
        {
          title: 'test',
          subMenu: [{ title: 'test' }],
        },
      ],
    });
    expect(wrapper.find('HintDropDown').exists()).toBeTruthy();
  });
  it('Проверяет отправку экшена', () => {
    const mockFn = sinon.spy();
    const { wrapper } = setup({
      callActionImpl: mockFn,
      buttons: [
        {
          title: 'test',
          action: 'test-action',
        },
      ],
    });

    wrapper.find('HintButton').simulate('click');
    expect(mockFn.calledOnce).toEqual(true);
    expect(mockFn.args[0][1]).toEqual({ action: 'test-action' });
  });
});
