import React from 'react';
import { storiesOf } from '@storybook/react';

import TextTableHeader from '../../headers/TextTableHeader';
import TextCell from './TextCell';
import Table from '../../Table';

const stories = storiesOf('Ячейки/Форматирование текста', module);

stories.addParameters({
  info: {
    propTables: [TextCell],
    propTablesExclude: [Table],
  },
});

stories
  .add('Компонент', () => {
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
          subTextFieldKey: 'sub_format_1',
          subTextFormat: 'date DD.MM.YYYY HH:mm',
        },
        {
          id: 'format_2',
          component: TextCell,
          format: 'date DD.MM.YYYY',
          subTextFieldKey: 'sub_format_2',
          subTextFormat: 'date DD.MM.YYYY',
        },
        {
          id: 'format_3',
          component: TextCell,
          format: 'password',
          subTextFieldKey: 'sub_format_3',
          subTextFormat: 'password',
        },
        {
          id: 'format_4',
          component: TextCell,
          format: 'number 0,0.00',
          subTextFieldKey: 'sub_format_4',
          subTextFormat: 'number 0,0.00',
        },
        {
          id: 'format_5',
          component: TextCell,
          format: 'dateFromNow DD.MM.YYYY',
          subTextFieldKey: 'sub_format_5',
          subTextFormat: 'dateFromNow DD.MM.YYYY',
        },
        {
          id: 'a.b.c',
          component: TextCell,
          subTextFieldKey: 'a.b.d',
        },
      ],
      datasource: [
        {
          format_1: '11-23-2017 17:32:23',
          format_2: '11-23-2017 17:32:23',
          format_3: 'fdb5g5g54g',
          format_4: '1234',
          format_5: '',
          tooltipFieldId: ['tooltip', 'body'],
          a: {
            b: {
              c: 'deep',
              d: 'deep_secondary',
            },
          },
          sub_format_1: '11-23-2017 17:32:23',
          sub_format_2: '11-23-2017 17:32:23',
          sub_format_3: 'fdb5g5g54g',
          sub_format_4: '1234',
          sub_format_5: '',
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
  })
  .add('withTooltip', () => {
    const tableProps = {
      headers: [
        {
          id: 'id_1',
          component: TextTableHeader,
          label: 'date DD.MM.YYYY HH:mm',
        },
      ],
      cells: [
        {
          id: 'format_1',
          component: TextCell,
          format: 'date DD.MM.YYYY HH:mm',
          tooltipFieldId: 'tooltip',
        },
        {
          id: 'format_2',
          component: TextCell,
          format: 'date DD.MM.YYYY',
        },
      ],
      datasource: [
        {
          tooltip: 'tooltip',
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
