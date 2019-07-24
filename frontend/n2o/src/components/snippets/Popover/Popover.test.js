import React from 'react';
import Popover from './Popover';

const props = {
  help: {
    id: 'test',
    help: 'подсказка',
  },
  confirm: {
    popConfirm: 'true',
  },
};

const setup = propsOverride => {
  return shallow(<Popover {...props.help} {...propsOverride} />);
};

const setupConfirm = propsOverride => {
  return shallow(<Popover {...props.confirm} {...propsOverride} />);
};

describe('Тесты Popover', () => {
  it('Отрисовывается, если переданы id и help', () => {
    const wrapper = setup();
    console.log(wrapper.debug());
    expect(wrapper.find('.n2o-popover').exists()).toEqual(true);
  });
  it('Показывает подсказку', () => {
    const wrapper = setup();
    wrapper.instance().onToggle();
    expect(wrapper.state().showPopover).toEqual(true);
  });
  it('onClickYes', () => {
    const wrapper = setupConfirm();
    console.log(wrapper.debug());
    wrapper
      .find('.btn-sm')
      .last()
      .simulate('click');
    expect(wrapper.state().answer).toEqual(true);
  });
});
