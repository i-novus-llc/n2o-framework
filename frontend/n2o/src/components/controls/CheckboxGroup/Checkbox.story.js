import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean, array } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import CheckboxGroup from './CheckboxGroup';
import Checkbox from '../Checkbox/Checkbox';

const stories = storiesOf('Контролы/Группа чекбоксов', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CheckboxGroup'));
stories.addDecorator(jsxDecorator);

stories.add('Компонент', () => {
  const props = {
    value: array('value', ['1', '2']),
    disabled: boolean('disabled', false),
    visible: boolean('visible', true),
    className: text('className', ''),
    inline: boolean('inline', false),
  };

  return (
    <CheckboxGroup name="numbers" {...props}>
      <Checkbox value="1" label="Первый" />
      <Checkbox value="2" label="Второй" />
      <Checkbox value="3" label="Третий" />
    </CheckboxGroup>
  );
});
