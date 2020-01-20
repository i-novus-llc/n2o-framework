import React from 'react';
import BadgeCell from './BadgeCell';

const setup = propsOverride => {
  const wrapper = mount(<BadgeCell {...propsOverride} />);

  return { wrapper };
};

describe('<BadgeCell />', () => {
  it('проверяет создание баджа', () => {
    const { wrapper } = setup();

    expect(wrapper.find('BadgeCell')).toBeTruthy();
  });
});
