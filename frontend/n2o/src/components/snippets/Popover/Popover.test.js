import React from 'react';
import { N2OPopover } from './Popover';
import sinon from 'sinon';

const props = {
  help: {
    id: 'test',
    help: 'подсказка',
  },
  component: {
    body: 'body',
    header: 'header',
  },
};

const setupHelp = propsOverride => {
  return shallow(<N2OPopover {...props.help} {...propsOverride} />);
};

const setupComponent = propsOverride => {
  return shallow(<N2OPopover {...props.component} {...propsOverride} />);
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
    const onClick = sinon.spy();
    const wrapper = setupHelp({ onToggle: onClick });
    wrapper.find('.toggle-popover').simulate('click');
    expect(onClick.called).toEqual(true);
  });
  it('Показывает popover', async () => {
    const onClick = sinon.spy();
    const wrapper = setupComponent({ onToggle: onClick });
    wrapper.find('.toggle-popover').simulate('click');
    expect(onClick.called).toEqual(true);
  });
  it('Проверка popConfirm', () => {
    const onClick = sinon.spy();
    const wrapper = setupComponent({ popConfirm: 'true', onClickYes: onClick });
    wrapper
      .find('.btn-sm')
      .last()
      .simulate('click');
    expect(onClick.called).toEqual(true);
  });
});
