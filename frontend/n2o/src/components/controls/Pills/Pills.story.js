import React from 'react';
import { storiesOf } from '@storybook/react';
import meta from './Pills.meta';
import PillsContainer from './PillsContainer';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Контролы/Кнопочные фильтры', module);

stories.addParameters({
  info: {
    propTables: [PillsContainer],
    propTablesExclude: [Factory],
  },
});

stories.add(
  'Компонент',
  () => {
    const props = {
      valueFieldId: meta.valueFieldId,
      labelFieldId: meta.labelFieldId,
      multiSelect: meta.multiSelect,
    };

    const data = [
      {
        id: 1,
        label: 'text',
      },
      {
        id: 2,
        label: 'text',
      },
    ];

    return <PillsContainer {...props} data={data} />;
  },
  {
    info: {
      text: `
      Компонент 'Кнопочные фильтры'
      ~~~js
      import Pills from 'n2o-framework/lib/components/controls/Pills/PillsContainer';
      
      const data = [
        {
          id: 1,
          label: 'text',
        },
        {
          id: 2,
          label: 'text',
        },
      ];  
      
      <PillsContainer 
          valueFieldId="id"
          labelFieldId="label"
          multiSelect={false}
          data={data}
       />
      ~~~
      `,
    },
  }
);
