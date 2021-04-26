import React from 'react'
import { storiesOf } from '@storybook/react'

import Table from '../../Table'

import SwitchCell from './SwitchCell'
import { model, modelFromDefaultView } from './switchCellStoryProps'
import switchCellJSON from './SwitchCell.meta'

const stories = storiesOf('Ячейки/Переключаемая ячейка', module)

stories.addParameters({
    info: {
        propTables: [SwitchCell],
        propTablesExclude: [Table],
    },
})

stories
    .add(
        'Компонент',
        () => <SwitchCell {...model} />,
        {
            info: {
                text: `
      ~~~js
      import SwitchCell from 'n2o-framework/lib/components/widgets/Table/cells/SwitchCell/SwitchCell';
      <SwitchCell
        props="метаданные"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Метаданные',
        () => <SwitchCell {...switchCellJSON} />,
        {
            info: {
                text: `
      ~~~js
      import SwitchCell from 'n2o-framework/lib/components/widgets/Table/cells/SwitchCell/SwitchCell';
      <SwitchCell
        props="метаданные"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'switchDefault',
        () => <SwitchCell {...modelFromDefaultView} />,
        {
            info: {
                text: `
      ~~~js
      import SwitchCell from 'n2o-framework/lib/components/widgets/Table/cells/SwitchCell/SwitchCell';
      <SwitchCell
        props="метаданные"
       />
      ~~~
      `,
            },
        },
    )
