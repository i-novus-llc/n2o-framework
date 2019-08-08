import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { action } from '@storybook/addon-actions';
import { withKnobs, text, boolean, array } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import CheckboxGroup from './CheckboxGroup';
import CheckboxButton from '../Checkbox/CheckboxButton';

const stories = storiesOf('Контролы/Группа чекбоксов', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CheckboxGroup'));
stories.addDecorator(jsxDecorator);

stories.add('Группа в виде кнопок', () => {
  const props = {
    value: array('value', ['1', '2']),
    disabled: boolean('disabled', false),
    visible: boolean('visible', true),
    className: text('className', ''),
    inline: boolean('inline', false),
  };

  return (
    <CheckboxGroup name="numbers" isBtnGroup={true} {...props}>
      <CheckboxButton value="1" label="Первый" />
      <CheckboxButton value="2" label="Второй" />
      <CheckboxButton value="3" label="Третий" />
    </CheckboxGroup>
  );
});
