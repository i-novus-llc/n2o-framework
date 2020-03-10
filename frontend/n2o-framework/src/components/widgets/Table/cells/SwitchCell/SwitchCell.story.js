import React from 'react';
import { storiesOf } from '@storybook/react';

import Table from '../../Table';
import SwitchCell from './SwitchCell';

import {
  model,
  modelFromDefaultView,
  tableProps,
} from './switchCellStoryProps';

import switchCellJSON from './SwitchCell.meta';

const stories = storiesOf('Ячейки/Переключаемая ячейка', module);

stories.addParameters({
  info: {
    propTables: [SwitchCell],
    propTablesExclude: [Table],
  },
});

stories
  .add(
    'Компонент',
    () => {
      return <SwitchCell model={model} />;
    },
    {
      info: {
        text: `
      ~~~js
      import SwitchCell from 'n2o-framework/lib/components/widgets/Table/cells/SwitchCell/SwitchCell';
      <SwitchCell
        model="модель данных"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'Метаданные',
    () => {
      return <SwitchCell model={switchCellJSON} />;
    },
    {
      info: {
        text: `
      ~~~js
      import SwitchCell from 'n2o-framework/lib/components/widgets/Table/cells/SwitchCell/SwitchCell';
      <SwitchCell
        model="модель данных"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'switchDefault',
    () => {
      return <SwitchCell model={modelFromDefaultView} />;
    },
    {
      info: {
        text: `
      ~~~js
      import SwitchCell from 'n2o-framework/lib/components/widgets/Table/cells/SwitchCell/SwitchCell';
      <SwitchCell
        model="модель данных"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'в Таблице',
    () => {
      return <Table {...tableProps} />;
    },
    {
      info: {
        text: `
      ~~~js
      import SwitchCell from 'n2o-framework/lib/components/widgets/Table/cells/SwitchCell/SwitchCell';
      <SwitchCell
        model="модель данных"
       />
      ~~~
      `,
      },
    }
  );
