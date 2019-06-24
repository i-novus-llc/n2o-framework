import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import fetchMock from 'fetch-mock';
import { parseUrl, getStubData } from 'N2oStorybook/fetchMock';
import SelectWrapper from './SelectWrapper';
import SelectJson from './SelectWrapper.meta.json';
import withForm from 'N2oStorybook/decorators/withForm';

const form = withForm({ src: 'N2OSelect' });
const stories = storiesOf('Контролы/Выпадающий список', module);
stories.addDecorator(withKnobs);

stories.add(
  'Метаданные',
  form(() => {
    const props = {
      value: text('value', SelectJson.value),
      visible: boolean('visible', SelectJson.visible),
      valueFieldId: text('valueFieldId', SelectJson.valueFieldId),
      labelFieldId: text('labelFieldId', SelectJson.labelFieldId),
      queryId: text('queryId', SelectJson.queryId),
      size: number('size', SelectJson.size),
      heightSize: text('heightSize', SelectJson.heightSize),
      disabled: boolean('disabled', SelectJson.disabled),
      autoFocus: boolean('autoFocus', SelectJson.autoFocus),
      required: boolean('required', SelectJson.required),
      dataProvider: SelectJson.dataProvider,
    };

    const list = [
      {
        id: '1',
        label: 'Alex',
      },
      {
        id: '2',
        label: 'lex',
      },
      {
        id: '3',
        label: 'ex',
      },
    ];

    fetchMock.restore().get('begin:n2o/data', { list });

    return props;
  })
);
