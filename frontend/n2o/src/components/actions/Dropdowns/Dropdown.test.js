import React from 'react';
import { mount, shallow } from 'enzyme';
import Dropdown from './Dropdown';

const setup = propsOverride => {
  const props = {
    title: 'some title',
    children: 'some children',
  };

  return mount(<Dropdown {...props} {...propsOverride} />);
};

describe('<Dropdown />', () => {
  it('компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('ButtonDropdown').exists()).toBeTruthy();
  });

  it('должны проставиться title и children', () => {
    const wrapper = setup();

    expect(wrapper.find('DropdownToggle').text()).toBe('some title');
    expect(wrapper.find('DropdownMenu').text()).toBe('some children');
  });
});
