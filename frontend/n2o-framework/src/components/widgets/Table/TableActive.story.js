import React from 'react';
import { storiesOf } from '@storybook/react';

import Table from './Table';
import TextTableHeader from './headers/TextTableHeader';
import TextCell from './cells/TextCell/TextCell';

const stories = storiesOf('Виджеты/Таблица', module);

stories.add('Активные записи', () => {
  const tableData = [
    { id: '1', name: 'Foo', surname: 'Bar', birthday: '01.01.2001' },
    { id: '2', name: 'X', surname: 'Y', birthday: '01.01.1001' },
    { id: '3', name: 'Test', surname: 'Tset', birthday: '01.01.0001' },
  ];

  const headers = [
    { id: 'name', component: TextTableHeader, sortable: false, label: 'Имя' },
    {
      id: 'surname',
      component: TextTableHeader,
      sortable: false,
      label: 'Фамилия',
    },
    {
      id: 'birthday',
      component: TextTableHeader,
      sortable: false,
      sorting: 'ASC',
      label: 'Дата рождения',
    },
  ];
  const cells = [
    { id: 'name', component: TextCell, fieldKey: 'name' },
    { id: 'surname', component: TextCell, fieldKey: 'surname' },
    { id: 'birthday', component: TextCell, fieldKey: 'birthday' },
  ];

  return (
    <React.Fragment>
      <h4>Без фокуса</h4>

      <Table
        datasource={tableData}
        headers={headers}
        cells={cells}
        hasFocus={false}
        hasSelect={false}
        isActive={true}
      />
      <h4>Только фокус</h4>

      <Table
        datasource={tableData}
        headers={headers}
        cells={cells}
        hasFocus={true}
        hasSelect={false}
        isActive={true}
      />

      <h4>С выбором записи</h4>

      <Table
        datasource={tableData}
        headers={headers}
        cells={cells}
        hasFocus={true}
        hasSelect={true}
        autoFocus={false}
        isActive={true}
      />

      <h4>С выбором записи (автофокус)</h4>

      <Table
        datasource={tableData}
        headers={headers}
        cells={cells}
        hasFocus={true}
        hasSelect={true}
        autoFocus={true}
        isActive={true}
      />
    </React.Fragment>
  );
});
