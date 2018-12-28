import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, text, boolean } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import RadioGroup from './RadioGroup';
import Radio from '../Radio/Radio';

const stories = storiesOf('Контролы/Радио', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('RadioGroup'));

stories.add('Радио группа', () => {
  const props = {
    value: text('value', '2'),
    disabled: boolean('disabled', false),
    visible: boolean('visible', true),
    className: text('className', ''),
    inline: boolean('inline', true)
  };

  return (
    <RadioGroup name="numbers" onChange={action('radio-on-change')} {...props}>
      <Radio value="1" label="Первый" />
      <Radio value="2" label="Второй" />
      <Radio value="3" label="Третий" />
    </RadioGroup>
  );
});
