import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean, array } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import CheckboxGroup from './CheckboxGroup';
import CheckboxAlt, {
  CheckboxN2O as CheckboxComponent,
} from '../Checkbox/CheckboxN2O';

const stories = storiesOf('Контролы/Группа чекбоксов', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CheckboxGroup'));
stories.addDecorator(jsxDecorator);
stories.addParameters({
  info: {
    propTables: [CheckboxComponent],
    propTablesExclude: [CheckboxAlt],
  },
});

stories.add('N2O группа', () => {
  const props = {
    value: array('value', ['1', '2']),
    disabled: boolean('disabled', false),
    visible: boolean('visible', true),
    className: text('className', ''),
    inline: boolean('inline', false),
  };

  return (
    <CheckboxGroup name="numbers" {...props}>
      <CheckboxAlt value="1" label="Первый" />
      <CheckboxAlt value="2" label="Второй" />
      <CheckboxAlt value="3" label="Третий" />
    </CheckboxGroup>
  );
});
