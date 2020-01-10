import React from 'react';
import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';
import withForm from 'N2oStorybook/decorators/withForm';
import Radio from '../Radio/RadioN2O';
import RadioWrapper from './RadioWrapper.meta.json';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Контролы/Радио', module);
const form = withForm({ src: 'RadioGroup' });

stories.addParameters({
  info: {
    propTables: [Radio],
    propTablesExclude: [Factory],
  },
});

stories.add(
  'Метаданные',
  form(() => {
    const list = [
      {
        id: 1,
        label: 'One',
      },
      {
        id: 2,
        label: 'Two',
      },
      {
        id: 3,
        label: 'Three',
      },
    ];

    const props = {
      disabled: RadioWrapper.disabled,
      visible: RadioWrapper.visible,
      className: RadioWrapper.className,
      inline: RadioWrapper.inline,
      valueFieldId: RadioWrapper.valueFieldId,
      labelFieldId: RadioWrapper.labelFieldId,
      size: RadioWrapper.size,
      type: RadioWrapper.type,
      dataProvider: RadioWrapper.dataProvider,
    };

    fetchMock.restore().get('begin:n2o/data', { list });

    return props;
  })
);
