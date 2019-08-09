import React from 'react';
import { storiesOf } from '@storybook/react';
import {
  withKnobs,
  text,
  boolean,
  select,
  number,
} from '@storybook/addon-knobs/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import fetchMock from 'fetch-mock';
import withForm from 'N2oStorybook/decorators/withForm';
import Radio from '../Radio/RadioN2O';
import RadioWrapper from './RadioWrapper.meta.json';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Контролы/Радио', module);
const form = withForm({ src: 'RadioGroup' });

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);

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
