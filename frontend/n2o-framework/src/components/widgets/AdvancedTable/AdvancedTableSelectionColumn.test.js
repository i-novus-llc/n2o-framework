import React from 'react';
import sinon from 'sinon';
import { AdvancedTableSelectionColumn } from './AdvancedTableSelectionColumn';

const setup = propsOverride => {
  const props = {};

  return mount(<AdvancedTableSelectionColumn {...props} {...propsOverride} />);
};

describe('<AdvancedTableSelectionColumn />', () => {
  it('компонент отрисовывается', () => {
    const wrapper = setup();

    expect(
      wrapper.find('.n2o-advanced-table-selection-item').exists()
    ).toBeTruthy();
  });

  it('onChange отрабатывает', () => {
    const onChange = sinon.spy();
    const wrapper = setup({
      onChange,
    });

    wrapper.find('input').simulate('change');
    expect(onChange.calledOnce).toBeTruthy();
  });
});
