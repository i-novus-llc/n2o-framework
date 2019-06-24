import React from 'react';
import { storiesOf } from '@storybook/react';
import {
  withKnobs,
  text,
  boolean,
  select,
  number,
} from '@storybook/addon-knobs/react';
import fetchMock from 'fetch-mock';
import withForm from 'N2oStorybook/decorators/withForm';
import RadioWrapper from './RadioWrapper.meta.json';

const stories = storiesOf('Контролы/Радио', module);
const form = withForm({ src: 'RadioGroup' });

stories.addDecorator(withKnobs);

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
      disabled: boolean('disabled', RadioWrapper.disabled),
      visible: boolean('visible', RadioWrapper.visible),
      className: text('className', RadioWrapper.className),
      inline: boolean('inline', RadioWrapper.inline),
      valueFieldId: text('valueFieldId', RadioWrapper.valueFieldId),
      labelFieldId: text('labelFieldId', RadioWrapper.labelFieldId),
      size: RadioWrapper.size,
      type: select('type', ['default', 'n2o', 'btn'], RadioWrapper.type),
      dataProvider: RadioWrapper.dataProvider,
    };

    fetchMock.restore().get('begin:n2o/data', { list });

    return props;
  })
);
