import React from 'react';
import { storiesOf } from '@storybook/react';

import Table from './Table';
import TextTableHeader from './headers/TextTableHeader';
import TextCell from './cells/TextCell/TextCell';

const stories = storiesOf('Виджеты/Таблица', module);

stories.add('Цвет строк', () => {
  const tableDefaultColorData = [
    { id: '1', name: 'primary', h1: 'текст', h2: 'текст' },
    { id: '2', name: 'secondary', h1: 'текст', h2: 'текст' },
    { id: '3', name: 'success', h1: 'текст', h2: 'текст' },
    { id: '4', name: 'danger', h1: 'текст', h2: 'текст' },
    { id: '5', name: 'warning', h1: 'текст', h2: 'текст' },
    { id: '6', name: 'info', h1: 'текст', h2: 'текст' },
  ];
  const tableAdvancedColorData = [
    { id: '7', name: 'blue', h1: 'текст', h2: 'текст' },
    { id: '8', name: 'purple', h1: 'текст', h2: 'текст' },
    { id: '9', name: 'indigo', h1: 'текст', h2: 'текст' },
    { id: '10', name: 'pink', h1: 'текст', h2: 'текст' },
    { id: '11', name: 'red', h1: 'текст', h2: 'текст' },
    { id: '12', name: 'orange', h1: 'текст', h2: 'текст' },
    { id: '13', name: 'yellow', h1: 'текст', h2: 'текст' },
    { id: '14', name: 'green', h1: 'текст', h2: 'текст' },
    { id: '15', name: 'teal', h1: 'текст', h2: 'текст' },
    { id: '16', name: 'cyan', h1: 'текст', h2: 'текст' },
  ];
  const headers = [
    {
      id: 'name',
      component: TextTableHeader,
      sortable: false,
      label: 'Цвет (класс)',
    },
    {
      id: 'h1',
      component: TextTableHeader,
      sortable: false,
      label: 'Заголовок',
    },
    {
      id: 'h2',
      component: TextTableHeader,
      sortable: false,
      label: 'Заголовок',
    },
  ];
  const cells = [
    { id: 'name', component: TextCell, fieldKey: 'name' },
    { id: 'h1', component: TextCell, fieldKey: 'h1' },
    { id: 'h2', component: TextCell, fieldKey: 'h2' },
  ];

  return (
    <React.Fragment>
      <h3>Стандартные цвета</h3>
      <Table
        datasource={tableDefaultColorData}
        headers={headers}
        cells={cells}
        isActive={true}
        hasFocus={true}
        hasSelect={false}
        rowColor="`id == 1 ? 'primary' : id == 2 ? 'secondary' : id == 3 ? 'success' : id == 4 ? 'danger' : id == 5 ? 'warning' : id == 6 ? 'info' : id == 7 ? 'blue' : id == 8 ? 'purple' : id == 9 ? 'indigo' : id == 10 ? 'pink' : id == 11 ? 'red' : id == 12 ? 'orange' : id == 13 ? 'yellow' : id == 14 ? 'green' : id == 15 ? 'teal' : 'cyan'`"
      />
      <h3>Дополнительные цвета</h3>
      <Table
        datasource={tableAdvancedColorData}
        headers={headers}
        cells={cells}
        isActive={true}
        hasFocus={true}
        hasSelect={false}
        rowColor="`id == 1 ? 'primary' : id == 2 ? 'secondary' : id == 3 ? 'success' : id == 4 ? 'danger' : id == 5 ? 'warning' : id == 6 ? 'info' : id == 7 ? 'blue' : id == 8 ? 'purple' : id == 9 ? 'indigo' : id == 10 ? 'pink' : id == 11 ? 'red' : id == 12 ? 'orange' : id == 13 ? 'yellow' : id == 14 ? 'green' : id == 15 ? 'teal' : 'cyan'`"
      />
    </React.Fragment>
  );
});
