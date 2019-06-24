import React from 'react';
import { storiesOf } from '@storybook/react';
import {
  withKnobs,
  text,
  boolean,
  object,
  select,
} from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import Table from '../../Table';
import progressBarStyles from './progressBarStyles';
import ProgressBarCell from './ProgressBarCell';
import TextTableHeader from '../../headers/TextTableHeader';
import ProgressBarJson from './ProgressBarCell.meta';

const stories = storiesOf('Ячейки/Индикатор', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('ProgressBarCell'));

stories.add('Метаданные', () => {
  const props = {
    id: text('id', ProgressBarJson.id),
    animated: boolean('animated', ProgressBarJson.animated),
    striped: boolean('striped', ProgressBarJson.striped),
    color: select(
      'color',
      Object.values(progressBarStyles),
      ProgressBarJson.color
    ),
    size: select('size', ['mini', 'default', 'large'], ProgressBarJson.size),
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
});
