import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { boolean, text, withKnobs } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import TextTableHeader from '../../headers/TextTableHeader';
import TextCell from './TextCell';
import Table from '../../Table';

const stories = storiesOf('Ячейки/Форматирование текста', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CheckboxCell'));
stories.addDecorator(jsxDecorator);
stories.addParameters({
  info: {
    propTables: [TextCell],
    propTablesExclude: [Table],
  },
});

stories.add('Компонент', () => {
  const tableProps = {
    headers: [
      {
        id: 'id_1',
        component: TextTableHeader,
        label: 'date DD.MM.YYYY HH:mm',
      },
      {
        id: 'id_2',
        component: TextTableHeader,
        label: 'date DD.MM.YYYY',
      },
      {
        id: 'id_3',
        component: TextTableHeader,
        label: 'password',
      },
      {
        id: 'id_4',
        component: TextTableHeader,
        label: 'number 0,0.00',
      },
      {
        id: 'id_5',
        component: TextTableHeader,
        label: 'dateFromNow DD.MM.YYYY',
      },
      {
        id: 'id_6',
        component: TextTableHeader,
        label: 'a.b.c',
      },
    ],
    cells: [
      {
        id: 'format_1',
        component: TextCell,
        format: 'date DD.MM.YYYY HH:mm',
      },
      {
        id: 'format_2',
        component: TextCell,
        format: 'date DD.MM.YYYY',
      },
      {
        id: 'format_3',
        component: TextCell,
        format: 'password',
      },
      {
        id: 'format_4',
        component: TextCell,
        format: 'number 0,0.00',
      },
      {
        id: 'format_5',
        component: TextCell,
        format: 'dateFromNow DD.MM.YYYY',
      },
      {
        id: 'a.b.c',
        component: TextCell,
      },
    ],
    datasource: [
      {
        format_1: '23.11.2017 17:32:23',
        format_2: '23.11.2017 17:32:23',
        format_3: 'fdb5g5g54g',
        format_4: '1234',
        format_5: '',
        a: {
          b: {
            c: 'deep',
          },
        },
      },
    ],
  };

  return (
    <Table
      headers={tableProps.headers}
      cells={tableProps.cells}
      datasource={tableProps.datasource}
    />
  );
});
