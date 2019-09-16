import React from 'react';
import sinon from 'sinon';
import ContentEditable from './ContentEditable';

const setup = props => {
  return mount(<ContentEditable {...props} />);
};

describe('Тесты ContentEditable', () => {
  // it('componentDidUpdate', () => {
  //   const focus = sinon.spy();
  //   const wrapper = setup({ focus: focus });
  //   console.log(wrapper.debug())
  //   expect(wrapper.props().editable).toBe(false);
  //   wrapper.setProps({ editable: true});
  //   expect(focus.calledOnce).toEqual(true); ///// ??????????????
  // });
  it('editable = true', () => {
    const wrapper = setup({ editable: true});
    expect(wrapper.find('.editable').exists()).toEqual(true);
  });
  it('editable = false', () => {
    const wrapper = setup({ editable: false });
    expect(wrapper.find('.editable').exists()).toEqual(false);
  });
});
