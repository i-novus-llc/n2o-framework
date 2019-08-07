import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { action } from '@storybook/addon-actions';
import { withKnobs, text, boolean, array } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import CheckboxGroup from './CheckboxGroup';
import CheckboxAlt from '../Checkbox/CheckboxN2O';

const stories = storiesOf('Контролы/Группа чекбоксов', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CheckboxGroup'));
stories.addDecorator(jsxDecorator);

stories.add('N2O группа', () => {
  const props = {
    value: array('value', ['1', '2']),
    disabled: boolean('disabled', false),
    visible: boolean('visible', true),
    className: text('className', ''),
    inline: boolean('inline', false),
  };

  return (
    <CheckboxGroup
      name="numbers"
      onChange={action('checkbox-on-change')}
      {...props}
    >
      <CheckboxAlt value="1" label="Первый" />
      <CheckboxAlt value="2" label="Второй" />
      <CheckboxAlt value="3" label="Третий" />
    </CheckboxGroup>
  );
});
