import React from 'react';
import Popover from './Popover';
import sinon from 'sinon';

const props = {
  help: {
    id: 'test',
    help: 'подсказка',
  },
  confirm: {
    popConfirm: 'true',
  },
  component: {
    body: 'body.',
    header: 'header',
  },
};

const setupHelp = propsOverride => {
  return shallow(<Popover {...props.help} {...propsOverride} />);
};

const setupConfirm = propsOverride => {
  return shallow(<Popover {...props.confirm} {...propsOverride} />);
};

const setupComponent = propsOverride => {
  return shallow(<Popover {...props.component} {...propsOverride} />);
};

describe('Тесты Popover', () => {
  it('Отрисовывается, если переданы id и help', () => {
    const wrapper = setupHelp();
    expect(wrapper.find('.n2o-popover').exists()).toEqual(true);
  });
  it('Отрисовывается, если переданы header и body', () => {
    const wrapper = setupComponent();
    expect(wrapper.find('.n2o-popover').exists()).toEqual(true);
  });
  it('Показывает подсказку', () => {
    const wrapper = setupHelp();
    wrapper.instance().onToggle();
    expect(wrapper.state().showPopover).toEqual(true);
  });
  it('Показывает popover', () => {
    const wrapper = setupComponent();
    wrapper.instance().onToggle();
    expect(wrapper.state().showPopover).toEqual(true);
  });
  it('Проверка popConfirm', () => {
    const onClick = sinon.spy();
    const wrapper = setupConfirm({ onConfirm: onClick });
    wrapper
      .find('.btn-sm')
      .last()
      .simulate('click');
    expect(onClick.called).toEqual(true);
  });
});
