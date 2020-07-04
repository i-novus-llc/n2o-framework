import React from 'react';
import AlertField from './AlertField';

const setup = propsOverride => {
  return mount(<AlertField {...propsOverride} />);
};

describe('Проверка AlertField', () => {
  it('visible = false', () => {
    const wrapper = setup({ visible: false });
    expect(wrapper.children().exists()).toEqual(false);
  });
});
