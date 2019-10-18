import React from 'react';
import NavbarBrandContent from './NavbarBrandContent';

const setup = props => {
  return mount(<NavbarBrandContent {...props} />);
};

describe('Тесты NavbarBrandContent', () => {
  it('Отрисовывается img', () => {
    const wrapper = setup({ brandImage: 'img' });
    expect(wrapper.find('img').props().src).toBe('img');
  });
  it('Отрисовывается без img', () => {
    const wrapper = setup({ brandImage: <div>test</div> });
    expect(wrapper.find('div').exists()).toEqual(true);
  });
});
