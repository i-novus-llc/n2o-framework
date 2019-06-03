import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean } from '@storybook/addon-knobs/react';
import fetchMock from 'fetch-mock';
import withForm from 'N2oStorybook/decorators/withForm';
import meta from './Pills.meta';
import PillsContainer from './PillsContainer';

const dataUrl = 'begin:n2o/data';

const stories = storiesOf('Контролы/Кнопочные фильтры', module);

const form = withForm({ src: 'Pills' });

stories.addDecorator(withKnobs);

const handleData = list => url => ({ list });

stories
  .add('Компонент', () => {
    const props = {
      valueFieldId: text('valueFieldId', meta.valueFieldId),
      labelFieldId: text('labelFieldId', meta.labelFieldId),
      multiSelect: boolean('multiSelect', meta.multiSelect),
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
  })
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
