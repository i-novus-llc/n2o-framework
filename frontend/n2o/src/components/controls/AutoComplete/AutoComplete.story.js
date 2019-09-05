import React from 'react';
import { storiesOf } from '@storybook/react';
import withForm from 'N2oStorybook/decorators/withForm';
import AutoComplete from './AutoComplete';

const stories = storiesOf('Контролы/AutoComplete', module);

const form = withForm({ src: 'AutoComplete' });

const options = [
  {
    id: 1,
    name: 'test1',
  },
  {
    id: 2,
    name: 'test2',
  },
  {
    id: 3,
    name: 'test3',
  },
];

stories.add('Компонент', () => {
  return (
    <AutoComplete valueFieldId="name" labelFieldId="name" options={options} />
  );
});
