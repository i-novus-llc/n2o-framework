import React from 'react';
import Drawer, { N2ODrawer } from './Drawer';
import sinon from 'sinon';

const props = {
  title: 'title',
  footer: 'footer',
  animation: true,
};

const setup = propsOverride => {
  return shallow(<N2ODrawer {...props} {...propsOverride} />);
};

describe('Тесты Drawer', () => {
  it('Отрисовывается компонент', () => {
    const wrapper = setup();
    expect(wrapper.find('.n2o-drawer').exists()).toEqual(true);
  });
  it('Отрисовывается title', () => {
    const wrapper = setup();
    expect(wrapper.find('.drawer-title').props().children).toBe(props.title);
  });
  it('Отрисовывается footer', () => {
    const wrapper = setup();
    expect(wrapper.find('.drawer-footer').props().children).toBe(props.footer);
  });
  it('animation true', () => {
    const wrapper = setup();
    expect(wrapper.find('.drawer-animation').exists()).toEqual(true);
  });
});
