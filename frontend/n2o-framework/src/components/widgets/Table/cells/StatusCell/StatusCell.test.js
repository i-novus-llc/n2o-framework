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
      },
    ],
    datasource: [{ test: 'test-text', tooltipFieldId: ['tooltip', 'body'] }],
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
  // it('s', () => {
  //   const wrapper = setup();
  //   expect(
  //     wrapper
  //       .find('.list-text-cell__trigger')
  //       .html()
  //       .exists()
  //       .toEqual(
  //         `<span class="list-text-cell__trigger"><span><span class="n2o-status-cell bg-info"></span>test-text</span></span>`
  //       )
  //   );
  // });
});
