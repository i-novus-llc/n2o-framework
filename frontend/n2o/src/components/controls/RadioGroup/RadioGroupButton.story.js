import React from 'react';
import { storiesOf } from '@storybook/react';

import withTests from 'N2oStorybook/withTests';

import RadioGroup from './RadioGroup';
import RadioButton from '../Radio/RadioButton';

const stories = storiesOf('Контролы/Радио', module);

stories.addDecorator(withTests('CheckboxGroup'));

stories.add('Группа в виде кнопок', () => {
  const props = {
    value: '2',
    disabled: false,
    visible: true,
    className: '',
    inline: true,
  };

  return (
    <RadioGroup name="numbers" isBtnGroup={true} onChange={() => {}} {...props}>
      <RadioButton value="1" label="Первый" />
      <RadioButton value="2" label="Второй" />
      <RadioButton value="3" label="Третий" />
    </RadioGroup>
  );
});
