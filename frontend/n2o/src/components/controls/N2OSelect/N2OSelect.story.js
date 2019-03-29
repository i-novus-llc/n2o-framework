import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import InputSelect from './N2OSelect';
import N2OSelectJson from './N2OSelect.meta.json';

const stories = storiesOf('Контролы/Выпадающий список', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('InputSelect'));

stories.add('N2O вариант', () => {
  const options = [
    {
      id: 'Алексей',
      icon: 'fa fa-square',
      image: 'https://i.stack.imgur.com/2zqqC.jpg'
    },
    {
      id: 'Игорь',
      icon: 'fa fa-plus',
      image: 'https://i.stack.imgur.com/2zqqC.jpg'
    },
    {
      id: 'Владимир',
      icon: 'fa fa-square',
      image: 'https://i.stack.imgur.com/2zqqC.jpg'
    },
    {
      id: 'Анатолий',
      icon: 'fa fa-square',
      image: 'https://i.stack.imgur.com/2zqqC.jpg'
    },
    {
      id: 'Николай',
      icon: 'fa fa-plus',
      image: 'https://i.stack.imgur.com/2zqqC.jpg'
    }
  ];

  const props = {
    loading: boolean('loading', N2OSelectJson.loading),
    value: text('value', N2OSelectJson.value),
    disabled: boolean('disabled', N2OSelectJson.disabled),
    placeholder: text('placeholder', N2OSelectJson.placeholder),
    hasSearch: boolean('hasSearch', N2OSelectJson.hasSearch),
    valueFieldId: text('valueFieldId', N2OSelectJson.valueFieldId),
    labelFieldId: text('labelFieldId', N2OSelectJson.labelFieldId),
    filter: text('filter', N2OSelectJson.filter),
    resetOnBlur: boolean('resetOnBlur', N2OSelectJson.resetOnBlur),
    iconFieldId: text('iconFieldId', N2OSelectJson.iconFieldId),
    imageFieldId: text('imageFieldId', N2OSelectJson.imageFieldId),
    groupFieldId: text('groupFieldId', N2OSelectJson.groupFieldId),
    hasCheckboxes: boolean('hasCheckboxes', N2OSelectJson.hasCheckboxes),
    closePopupOnSelect: boolean('closePopupOnSelect', N2OSelectJson.closePopupOnSelect),
    cleanable: boolean('cleanable', N2OSelectJson.cleanable),
    format: text('format', N2OSelectJson.format)
  };

  return <InputSelect {...props} options={options} />;
});
