import React from 'react';
import { storiesOf } from '@storybook/react';

import fetchMock from 'fetch-mock';
import WrapperJson from './CheckboxWrapper.meta.json';
import withForm from 'N2oStorybook/decorators/withForm';

const form = withForm({ src: 'CheckboxGroup' });

const stories = storiesOf('Контролы/Группа чекбоксов', module);

stories.add(
  'Метаданные',
  form(() => {
    const list = [
      {
        id: 1,
        label: 'One',
        value: 'One',
      },
      {
        id: 2,
        label: 'Two',
        value: 'Two',
      },
      {
        id: 3,
        label: 'Three',
        value: 'Three',
      },
    ];

    const props = {
      disabled: WrapperJson.disabled,
      visible: WrapperJson.visible,
      className: WrapperJson.className,
      inline: WrapperJson.inline,
      valueFieldId: WrapperJson.valueFieldId,
      labelFieldId: WrapperJson.labelFieldId,
      size: WrapperJson.size,
      type: WrapperJson.type,
      dataProvider: WrapperJson.dataProvider,
    };

    fetchMock.restore().get('begin:n2o/data', { list });

    return props;
  })
);
