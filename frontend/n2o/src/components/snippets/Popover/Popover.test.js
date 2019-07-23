import React from 'react';
import Popover from './Popover';

const setupPopover = propsOverride => {
  const props = {
    id: 'test',
    help: 'подсказка',
  };
  return shallow(<Popover {...props} {...propsOverride} />);
};

describe('Тесты Popover', () => {
  it('Отрисовывается, если переданы id и help', () => {
    const wrapper = setupPopover();
    expect(wrapper.find('.n2o-popover').exists()).toEqual(true);
  });
  it('Показывает подсказку', () => {
    const wrapper = setupPopover();
    wrapper.find('.n2o-popover-btn').simulate('click');
    expect(wrapper.find('.n2o-popover-body').exists()).toEqual(true);
  });
});
