import React from 'react';
import Card from './Card';

const setupComponent = propsOverride => {
  return mount(<Card {...propsOverride} />);
};

describe('Тесты Card', () => {
  it('Отрисовка без children', () => {
    const wrapper = setupComponent();
    expect(wrapper.find('.n2o-card-wrap'));
  });
  it('Отрисовка с children', () => {
    const wrapper = setupComponent({ children: 'children' });
    expect(wrapper.find('children'));
  });
});
