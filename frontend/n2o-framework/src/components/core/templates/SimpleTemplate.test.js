import React from 'react';
import { mount, shallow } from 'enzyme';
import SimpleTemplate from './SimpleTemplate';

const setup = propsOverride => {
  const props = {};

  return mount(<SimpleTemplate {...props} {...propsOverride} />);
};

describe('<SimpleTemplate />', () => {
  it('компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('.application').exists()).toBeTruthy();
  });
});
