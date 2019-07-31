import React from 'react';
import sinon from 'sinon';
import AdvancedTable from './AdvancedTable';

const setup = propsOverride => {
  const props = {
    columns: [
      {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        width: '100px',
      },
      {
        title: 'Surname',
        dataIndex: 'surname',
        key: 'surname',
        width: '200px',
      },
      {
        title: 'Age',
        dataIndex: 'age',
        key: 'age',
        width: '50px',
      },
    ],
    data: [
      {
        id: 1,
        name: 'name1',
        surname: 'surname1',
        age: 1,
      },
      {
        id: 2,
        name: 'name2',
        surname: 'surname2',
        age: 2,
      },
      {
        id: 3,
        name: 'name3',
        surname: 'surname3',
        age: 3,
      },
    ],
  };

  return mount(<AdvancedTable {...props} {...propsOverride} />);
};

describe('<AdvancedTable/>', () => {
  it('срабатывает rowClick', () => {
    const onResolve = sinon.spy();
    const onRowClickAction = sinon.spy();

    const wrapper = setup({
      rowClick: true,
      onResolve,
      onRowClickAction,
    });

    wrapper
      .find('.n2o-table-row')
      .first()
      .simulate('click');

    expect(onResolve.calledOnce).toBe(true);
    expect(onRowClickAction.calledOnce).toBe(true);
  });

  it('срабатывает rowClick 2 и более раз по одной и той же строке', () => {
    const onResolve = sinon.spy();
    const onRowClickAction = sinon.spy();

    const wrapper = setup({
      rowClick: true,
      onResolve,
      onRowClickAction,
    });

    const tableRow = wrapper.find('.n2o-table-row').first();

    tableRow.simulate('click');

    expect(onResolve.calledOnce).toBe(true);
    expect(onRowClickAction.calledOnce).toBe(true);

    tableRow.simulate('click');

    expect(onResolve.calledTwice).toBe(true);
    expect(onRowClickAction.calledTwice).toBe(true);

    tableRow.simulate('click');

    expect(onResolve.calledThrice).toBe(true);
    expect(onRowClickAction.calledThrice).toBe(true);
  });

  it('в onResolve приходит правильная строка', () => {
    const onResolve = sinon.spy();

    const wrapper = setup({
      onResolve,
      isActive: true,
      hasSelect: true,
    });

    const rows = wrapper.find('.n2o-table-row');

    rows.at(1).simulate('click');

    expect(onResolve.calledOnce).toBe(true);
    expect(onResolve.getCall(0).args[0]).toEqual(wrapper.props().data[1]);

    rows.first().simulate('click');

    expect(onResolve.calledTwice).toBe(true);
    expect(onResolve.getCall(1).args[0]).toEqual(wrapper.props().data[0]);

    rows.at(2).simulate('click');

    expect(onResolve.calledThrice).toBe(true);
    expect(onResolve.getCall(2).args[0]).toEqual(wrapper.props().data[2]);
  });
});
