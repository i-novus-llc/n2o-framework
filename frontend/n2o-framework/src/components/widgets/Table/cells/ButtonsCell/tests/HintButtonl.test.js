import React from 'react';
import { mount } from 'enzyme';
import HintButton from '../HintButton';
import sinon from 'sinon';

const setup = (propOverrides = {}) => {
  const props = Object.assign(
    {
      visible: true,
      disabled: false,
    },
    propOverrides
  );

  const wrapper = mount(<HintButton {...props} />);

  return {
    props,
    wrapper,
  };
};

describe('<HintButton />', () => {
  it('проверяет создание элемента', () => {
    const { wrapper } = setup();

    expect(wrapper.find('Button').exists()).toBeTruthy();
    expect(wrapper.find('button.btn-link').exists()).toBeTruthy();
    expect(wrapper.find('button.btn-sm').exists()).toBeTruthy();
  });

  it('Свойство title', () => {
    const { wrapper } = setup({ title: 'test' });
    expect(wrapper.find('Button').props().children[1]).toBe('test');
  });

  it('Свойство icon', () => {
    const { wrapper } = setup({ icon: 'test' });
    expect(wrapper.find('i.test').exists()).toBeTruthy();
  });

  it('Свойство visible', () => {
    const { wrapper } = setup({ visible: false });
    expect(wrapper.find('Button').length).toBe(0);
  });

  it('Свойство disabled', () => {
    const { wrapper } = setup({ disabled: true });
    expect(wrapper.find('button.disabled').exists()).toBeTruthy();
  });

  it('Свойство size', () => {
    const { wrapper } = setup({ size: 'md' });
    expect(wrapper.find('button.btn-md').exists()).toBeTruthy();
  });

  it('Свойство color', () => {
    const { wrapper } = setup({ color: 'primary' });
    expect(wrapper.find('button.btn-primary').exists()).toBeTruthy();
  });

  it('Проверка события onClick если есть action', () => {
    const mockClick = sinon.spy();
    const mockEvent = { target: {} };
    const { wrapper } = setup({ action: 'test', onClick: mockClick });
    wrapper.simulate('click', mockEvent);
    wrapper.update();
    expect(mockClick.calledOnce).toEqual(true);
    expect(mockClick.args[0][1]).toEqual('test');
  });
});
