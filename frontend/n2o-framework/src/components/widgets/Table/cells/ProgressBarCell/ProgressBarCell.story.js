import React from 'react';
import { storiesOf } from '@storybook/react';

import Table from '../../Table';
import progressBarStyles from './progressBarStyles';
import ProgressBarCell from './ProgressBarCell';
import TextTableHeader from '../../headers/TextTableHeader';
import ProgressBarJson from './ProgressBarCell.meta';
import Factory from '../../../../../core/factory/Factory';

const stories = storiesOf('Ячейки/Индикатор', module);

stories.addParameters({
  info: {
    propTables: [ProgressBarCell],
    propTablesExclude: [Table, Factory],
  },
});

stories
  .add('Метаданные', () => {
    const props = {
      id: ProgressBarJson.id,
      animated: ProgressBarJson.animated,
      striped: ProgressBarJson.striped,
      color: ProgressBarJson.color,
      size: ProgressBarJson.size,
      model: {
        now: '12',
      },
    };

    const tableProps = {
      headers: [
        {
          id: 'header',
          component: TextTableHeader,
          label: 'Прогресс бар',
        },
      ],
      cells: [
        {
          component: ProgressBarCell,
          ...props,
        },
      ],
      datasource: [
        {
          id: 'now',
          now: 55,
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
    const props = {
      id: ProgressBarJson.id,
      animated: ProgressBarJson.animated,
      striped: ProgressBarJson.striped,
      color: ProgressBarJson.color,
      size: ProgressBarJson.size,
      tooltipFieldId: 'tooltip',
    };

    const tableProps = {
      headers: [
        {
          id: 'header',
          component: TextTableHeader,
          label: 'Прогресс бар',
        },
      ],
      cells: [
        {
          component: ProgressBarCell,
          ...props,
        },
      ],
      datasource: [
        {
          id: 'now',
          now: 55,
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
