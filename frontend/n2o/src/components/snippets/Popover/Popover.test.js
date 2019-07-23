import React from 'react';
import Popover from './Popover';

const props = {
  id: 'test',
  help: 'подсказка',
};

const setup = propsOverride => {
  return shallow(<Popover {...props} {...propsOverride} />);
};

describe('Тесты Popover', () => {
  it('Отрисовывается, если переданы id и help', () => {
    const wrapper = setup();
    expect(wrapper.find('.n2o-popover').exists()).toEqual(true);
  });
  it('Показывает подсказку', () => {
    const wrapper = setup();
    wrapper.instance().onToggle();
    expect(wrapper.state().showPopover).toEqual(true);
  });
});
