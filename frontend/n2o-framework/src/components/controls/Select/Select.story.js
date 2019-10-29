import React from 'react';
import { storiesOf } from '@storybook/react';
import Select from './Select';
import Option from './Option';

const stories = storiesOf('Контролы/Выпадающий список', module);

stories.add('Компонент', () => {
  const props = {
    value: 2,
    disabled: false,
    visible: true,
    heightSize: '',
  };

  const options = [
    { value: 1, label: 'Первый' },
    { value: 2, label: 'Второй' },
    { value: 3, label: 'Третий' },
  ];

  return (
    <Select onChange={() => {}} {...props}>
      {options.map((option, i) => (
        <Option key={i} value={option.value} label={option.label} />
      ))}
    </Select>
  );
});
