import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';
import fetchMock from 'fetch-mock';
import WrapperJson from './CheckboxWrapper.meta.json';
import withForm from 'N2oStorybook/decorators/withForm';

const form = withForm({ src: 'CheckboxGroup' });

const stories = storiesOf('Контролы/Группа чекбоксов', module);
stories.addDecorator(withKnobs);

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
      disabled: boolean('disabled', WrapperJson.disabled),
      visible: boolean('visible', WrapperJson.visible),
      className: text('className', WrapperJson.className),
      inline: boolean('inline', WrapperJson.inline),
      valueFieldId: text('valueFieldId', WrapperJson.valueFieldId),
      labelFieldId: text('labelFieldId', WrapperJson.labelFieldId),
      size: WrapperJson.size,
      type: select('type', ['default', 'n2o', 'btn'], WrapperJson.type),
      dataProvider: WrapperJson.dataProvider,
    };

    fetchMock.restore().get('begin:n2o/data', { list });

    return props;
  })
);
