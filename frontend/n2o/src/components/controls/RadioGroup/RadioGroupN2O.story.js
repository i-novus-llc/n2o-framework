import React from 'react';
import { storiesOf } from '@storybook/react';

import withTests from 'N2oStorybook/withTests';

import RadioGroup from './RadioGroup';
import RadioN2O from '../Radio/RadioN2O';

const stories = storiesOf('Контролы/Радио', module);

stories.addDecorator(withTests('RadioGroup'));

stories.add('N2O радио группа', () => {
  const props = {
    value: '2',
    disabled: false,
    visible: true,
    className: '',
    inline: true,
  };

  return (
    <RadioGroup name="numbers" onChange={() => {}} {...props}>
      <RadioN2O value="1" label="Первый" />
      <RadioN2O value="2" label="Второй" />
      <RadioN2O value="3" label="Третий" />
    </RadioGroup>
  );
});
