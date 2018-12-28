import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text } from '@storybook/addon-knobs/react';
import Input from './InputHidden';
import InputHiddenMeta from './InputHidden.meta';

const stories = storiesOf('Контролы/Скрытое поле', module);
stories.addDecorator(withKnobs);

stories
  .addWithJSX('Компонент', () => {
    const props = {
      value: text('Значение', 'InputHidden value')
    };

    return (
      <div>
        <p>Здесь находиться скрытое поле</p>
        <Input {...props} />
      </div>
    );
  })
  .addWithJSX('Метаданные', () => {
    return (
      <div>
        <p>Здесь находиться скрытое поле</p>
        <Input {...InputHiddenMeta} />
      </div>
    );
  });
