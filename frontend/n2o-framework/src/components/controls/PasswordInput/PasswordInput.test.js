import React from 'react';
import sinon from 'sinon';

import InputSelect from './PasswordInput';

const setup = propsOverrides => {
  const props = Object.assign({}, propsOverrides);

  const wrapper = mount(<InputSelect {...props} />);

  return {
    props,
    wrapper,
  };
};

describe('<PasswordInput />', () => {
  it('проверяет создание элемента', () => {
    const { wrapper } = setup();

    expect(wrapper.find('.n2o-input-password').exists()).toBeTruthy();
  });

  it('проверяет параметр showPasswordBtn', () => {
    const { wrapper } = setup({ showPasswordBtn: true });

    expect(wrapper.find('.n2o-input-password-toggler').exists()).toBeTruthy();
  });

  it('проверяет состояния showPass', () => {
    const { wrapper } = setup({ showPasswordBtn: true });
    const button = wrapper.find('.n2o-input-password-toggler').last();
    button.simulate('click');
    expect(wrapper.find('input').props().type).toBe('text');
    button.simulate('click');
    expect(wrapper.find('input').props().type).toBe('password');
  });
});
