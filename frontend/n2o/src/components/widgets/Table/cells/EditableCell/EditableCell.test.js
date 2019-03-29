import React from 'react';
import { EditableCell } from './EditableCell';

const setup = propsOverride => {
  const props = {
    visible: true,
    editable: true,
    value: 'test',
  };

  return mount(<EditableCell {...props} {...propsOverride} />);
};

describe('Тесты EditableCell', function() {
  it('компонент отрисовывается', () => {
    const wrapper = setup();
    expect(wrapper.find('.n2o-editable-cell').exists()).toEqual(true);
  });
  it('компонент не отрисовывается по visible = false', () => {
    const wrapper = setup({ visible: false });
    expect(wrapper.find('.n2o-editable-cell').exists()).toEqual(false);
  });
  it('отрисовывается контрол', () => {
    const wrapper = setup({
      control: {
        component: () => <div>test</div>,
      },
    });
    expect(wrapper.find('.n2o-editable-cell-control').exists()).toEqual(false);
    wrapper.setState({ editing: true });
    expect(wrapper.find('.n2o-editable-cell-control').exists()).toEqual(true);
  });
});
