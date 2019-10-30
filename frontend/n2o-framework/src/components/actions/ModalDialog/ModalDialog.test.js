import React from 'react';
import { mount, shallow } from 'enzyme';
import ModalDialog from './ModalDialog';
import sinon from 'sinon';

const setup = propsOverride => {
  const props = {
    visible: true,
  };

  return mount(<ModalDialog {...props} {...propsOverride} />);
};

describe('<ModalDialog />', () => {
  it('компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('Modal').exists()).toBeTruthy();
  });

  it('должны проставится title, text, okLabel, cancelLabel', () => {
    const wrapper = setup({
      title: 'Title',
      text: 'Text',
      okLabel: 'OK',
      cancelLabel: 'CANCEL',
    });

    expect(wrapper.find('ModalHeader').text()).toBe('Title');
    expect(wrapper.find('ModalBody').text()).toBe('Text');
    expect(
      wrapper
        .find('Button')
        .first()
        .text()
    ).toBe('OK');
    expect(
      wrapper
        .find('Button')
        .last()
        .text()
    ).toBe('CANCEL');
  });

  it('должен вызываться onConfirm', () => {
    const onConfirm = sinon.spy();
    const wrapper = setup({
      onConfirm,
    });

    wrapper
      .find('Button')
      .first()
      .simulate('click');
    expect(onConfirm.calledOnce).toBeTruthy();
  });

  it('должен вызываться onConfirm', () => {
    const onDeny = sinon.spy();
    const wrapper = setup({
      onDeny,
    });

    wrapper
      .find('Button')
      .last()
      .simulate('click');
    expect(onDeny.calledOnce).toBeTruthy();
  });
});
