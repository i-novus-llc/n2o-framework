import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, text, boolean } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import RadioGroup from './RadioGroup';
import RadioButton from '../Radio/RadioButton';

const stories = storiesOf('Контролы/Радио', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CheckboxGroup'));

stories.add('Группа в виде кнопок', () => {
  const props = {
    value: text('value', '2'),
    disabled: boolean('disabled', false),
    visible: boolean('visible', true),
    className: text('className', ''),
    inline: boolean('inline', true)
  };

  return (
    <RadioGroup name="numbers" isBtnGroup={true} onChange={action('checkbox-on-change')} {...props}>
      <RadioButton value="1" label="Первый" />
      <RadioButton value="2" label="Второй" />
      <RadioButton value="3" label="Третий" />
    </RadioGroup>
  );
});
