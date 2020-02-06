import React from 'react';

import StatusCell from './StatusCell';
import TextTableHeader from '../../headers/TextTableHeader';
import Table from '../../Table';

const setup = propOverrides => {
  const props = {
    headers: [
      {
        id: 'StatusCell',
        component: TextTableHeader,
        label: 'StatusCell',
      },
    ],
    cells: [
      {
        id: 'secondary',
        component: StatusCell,
        color: 'info',
        fieldKey: 'test',
        visible: true,
      },
    ],
    datasource: [{ test: 'test-text' }],
  };

  return mount(
    <Table
      headers={props.headers}
      cells={props.cells}
      datasource={props.datasource}
      redux={false}
      {...propOverrides}
    />
  );
};

describe('Тесты <StatusCell />', () => {
  it('отрисовка при color equal true с текстом', () => {
    const wrapper = setup();

    expect(wrapper.find('.bg-info').exists()).toEqual(true);
    expect(wrapper.find('StatusCell').contains('test-text')).toEqual(true);
  });
});
