import React from 'react';
import { mount } from 'enzyme';

import RadioN2O from './RadioN2O';

const setup = propsOverrides => {
  const props = Object.assign(
    {
      value: '123',
      checked: false,
    },
    propsOverrides
  );

  const wrapper = mount(<RadioN2O {...props} />);

  return {
    wrapper,
    props,
  };
};

describe('<RadioN2O />', () => {
  it('создание радио', () => {
    const { wrapper } = setup();

    expect(wrapper.find('input[type="radio"]').exists()).toBeTruthy();
  });

  it('классы радио', () => {
    const { wrapper } = setup({ name: 'name' });

    expect(wrapper.find('div.custom-control').exists()).toBeTruthy();
    expect(wrapper.find('div.custom-radio').exists()).toBeTruthy();
    expect(wrapper.find('input.custom-control-input').exists()).toBeTruthy();
    expect(wrapper.find('label.custom-control-label').exists()).toBeTruthy();
  });
});
