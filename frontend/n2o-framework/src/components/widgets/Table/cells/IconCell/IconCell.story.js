import React from 'react';
import { storiesOf } from '@storybook/react';
import { select, text, withKnobs } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import IconJson from './IconCell.meta.json';
import Table from '../../Table';
import IconCell from './IconCell';
import TextTableHeader from '../../headers/TextTableHeader';
import { iconCellTypes, textPlaceTypes } from './cellTypes';

const stories = storiesOf('Ячейки/Иконки', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('IconCell'));

stories.add('Метаданные', () => {
  const props = {
    id: text('id', IconJson.id),
    type: select('type', Object.values(iconCellTypes), IconJson.type),
    textPlace: select(
      'textPlace',
      Object.values(textPlaceTypes),
      IconJson.textPlace
    ),
    icon: text('icon', IconJson.icon),
    model: {
      name: 'Иван',
      age: '12',
    },
  };

  const tableProps = {
    headers: [
      {
        id: 'id',
        component: TextTableHeader,
        label: 'Ячейка с иконкой',
      },
    ],
    cells: [
      {
        component: IconCell,
        ...props,
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
});
