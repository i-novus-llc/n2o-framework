import React from 'react';
import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';
import withForm from 'N2oStorybook/decorators/withForm';
import meta from './Pills.meta';
import PillsContainer from './PillsContainer';
import Factory from '../../../core/factory/Factory';

const dataUrl = 'begin:n2o/data';

const stories = storiesOf('Контролы/Кнопочные фильтры', module);

const form = withForm({ src: 'Pills' });

stories.addParameters({
  info: {
    propTables: [PillsContainer],
    propTablesExclude: [Factory],
  },
});

const handleData = list => url => ({ list });

stories
  .add(
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
      import Pills from 'n2o/lib/components/controls/Pills/PillsContainer';
      
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
  )
  .add(
    'Метаданные',
    form(() => {
      const data = [
        {
          id: 1,
          label: 'Фамилия',
        },
        {
          id: 2,
          label: 'Имя',
        },
        {
          id: 3,
          label: 'Отчество',
        },
      ];

      fetchMock.get(dataUrl, handleData(data));

      return meta;
    })
  );
