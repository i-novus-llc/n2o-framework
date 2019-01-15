import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, text, boolean, number, array, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import Select from './Select';
import Option from './Option';

const stories = storiesOf('Контролы/Выпадающий список', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Select'));

stories.add('Компонент', () => {
  const props = {
    value: number('value', 2),
    disabled: boolean('disabled', false),
    visible: boolean('visible', true),
    heightSize: select('heightSize', ['input-lg', 'input-sm', ''], '')
  };

  const options = [
    { value: 1, label: 'Первый' },
    { value: 2, label: 'Второй' },
    { value: 3, label: 'Третий' }
  ];

  return (
    <Select onChange={action('select-on-change')} {...props}>
      {options.map((option, i) => (
        <Option key={i} value={option.value} label={option.label} />
      ))}
    </Select>
  );
});
