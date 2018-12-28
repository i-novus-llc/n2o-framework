import React from 'react';
import { shallow } from 'enzyme';

import CheckboxN2O from './CheckboxN2O';

const setup = propsOverrides => {
  const props = Object.assign(
    {
      value: '123',
      checked: false
    },
    propsOverrides
  );

  const wrapper = mount(<CheckboxN2O {...props} />);

  return {
    wrapper,
    props
  };
};

describe('<CheckboxN2O />', () => {
  it('создание чекбокса', () => {
    const { wrapper } = setup();

    expect(wrapper.find('input[type="checkbox"]').exists()).toBeTruthy();
  });

  it('классы чекбокса', () => {
    const { wrapper } = setup({ name: 'name' });

    expect(wrapper.find('div.custom-control').exists()).toBeTruthy();
    expect(wrapper.find('div.custom-checkbox').exists()).toBeTruthy();
    expect(wrapper.find('input.custom-control-input').exists()).toBeTruthy();
    expect(wrapper.find('label.custom-control-label').exists()).toBeTruthy();
  });
});
