import React from 'react';
import { storiesOf } from '@storybook/react';

import Table from '../../Table';
import TextTableHeader from '../../headers/TextTableHeader';
import CollapsedCell from './CollapsedCell';
import CollapsedCellJson from './CollapsedCell.meta.json';
import Factory from '../../../../../core/factory/Factory';

const stories = storiesOf('Ячейки/CollapsedCell', module);

stories.addParameters({
  info: {
    propTables: [CollapsedCell],
    propTablesExclude: [Factory, Table],
  },
});

stories
  .add('Метаданные', () => {
    const props = {
      fieldKey: CollapsedCellJson.fieldKey,
      color: CollapsedCellJson.color,
      amountToGroup: CollapsedCellJson.amountToGroup,
    };

    const tableProps = {
      headers: [
        {
          id: 'id',
          component: TextTableHeader,
          label: 'Ячейка',
        },
      ],
      cells: [
        {
          component: CollapsedCell,
          ...props,
        },
      ],
      datasource: [
        {
          id: 'id',
          name: 'alesha',
          data: ['Казань', 'Москва', 'Токио', 'Берлин', 'Париж', 'Лондон'],
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

  .add('Компонент', () => {
    const tableProps = {
      headers: [
        {
          id: 'id',
          component: TextTableHeader,
          label: 'Стандартная',
        },
        {
          id: 'id',
          component: TextTableHeader,
          label: 'Цветная',
        },
        {
          id: 'id',
          component: TextTableHeader,
          label: 'Мало элементов',
        },
      ],
      cells: [
        {
          component: CollapsedCell,
          model: {
            data: ['Казань', 'Москва', 'Токио', 'Берлин', 'Париж', 'Лондон'],
          },
          fieldKey: 'data',
        },
        {
          component: CollapsedCell,
          model: {
            data: ['Казань', 'Москва', 'Токио', 'Берлин'],
          },
          fieldKey: 'data',
          color: 'info',
        },
        {
          component: CollapsedCell,
          model: {
            data: ['Казань', 'Москва', 'Токио'],
          },
          fieldKey: 'data',
        },
      ],
      datasource: [
        {
          id: 'id',
          name: 'alesha',
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
  .add('С тултипом', () => {
    const tableProps = {
      headers: [
        {
          id: 'id',
          component: TextTableHeader,
          label: 'Стандартная',
        },
        {
          id: 'id',
          component: TextTableHeader,
          label: 'Цветная',
        },
        {
          id: 'id',
          component: TextTableHeader,
          label: 'Мало элементов',
        },
      ],
      cells: [
        {
          component: CollapsedCell,
          tooltipFieldId: 'tooltip',
          model: {
            data: ['Казань', 'Москва', 'Токио', 'Берлин', 'Париж', 'Лондон'],
            tooltip: 'tooltip',
          },
          fieldKey: 'data',
        },
        {
          component: CollapsedCell,
          model: {
            data: ['Казань', 'Москва', 'Токио', 'Берлин'],
          },
          fieldKey: 'data',
          color: 'info',
        },
        {
          component: CollapsedCell,
          model: {
            data: ['Казань', 'Москва', 'Токио'],
            tooltipFieldId: ['tooltip', 'body'],
          },
          fieldKey: 'data',
        },
      ],
      datasource: [
        {
          id: 'id',
          name: 'alesha',
          tooltip: 'tooltip',
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
