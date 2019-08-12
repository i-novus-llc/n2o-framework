import React from 'react';
import { storiesOf } from '@storybook/react';
import Input from './InputHidden';
import InputHiddenMeta from './InputHidden.meta';

const stories = storiesOf('Контролы/Скрытое поле', module);

stories
  .add('Компонент', () => {
    const props = {
      value: 'InputHidden value',
    };

    return (
      <div>
        <p>Здесь находиться скрытое поле</p>
        <Input {...props} />
      </div>
    );
  })
  .add('Метаданные', () => {
    return (
      <div>
        <p>Здесь находиться скрытое поле</p>
        <Input {...InputHiddenMeta} />
      </div>
    );
  });
