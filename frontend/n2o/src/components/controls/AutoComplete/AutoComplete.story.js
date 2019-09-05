import React from 'react';
import { storiesOf } from '@storybook/react';
import AutoComplete from './AutoComplete';

const stories = storiesOf('Контролы/AutoComplete', module);

const options = [
  {
    id: 1,
    label: 'test1',
  },
  {
    id: 2,
    label: 'test2',
  },
  {
    id: 3,
    label: 'test3',
  },
];

stories.add('Компонент', () => {
  return <AutoComplete options={options} />;
});
