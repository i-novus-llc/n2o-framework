import React from 'react';
import { mount } from 'enzyme';

import RadioButton from './RadioButton';

const setup = propsOverrides => {
  const props = Object.assign(
    {
      value: '123',
      checked: false,
    },
    propsOverrides
  );

  const wrapper = mount(<RadioButton {...props} />);

  return {
    wrapper,
    props,
  };
};

describe('<RadioButton />', () => {
  it('создание радио', () => {
    const { wrapper } = setup();

    expect(wrapper.find('input[type="radio"]').exists()).toBeTruthy();
  });

  it('классы чекбокса', () => {
    const { wrapper } = setup();

    expect(wrapper.find('label.btn').exists()).toBeTruthy();
  });
});
