import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withForm from 'N2oStorybook/decorators/withForm';
import TextArea from './TextArea';
import TextAreaJson from './TextArea.meta.json';

const form = withForm({ src: 'TextArea' });

const stories = storiesOf('Контролы/Многострочное текстовое поле', module);

stories.addDecorator(withKnobs);

stories
  .addWithJSX('Компонент', () => {
    const props = {
      placeholder: text('placeholder', 'Введите значение'),
      disabled: boolean('disabled', false),
      disabled: boolean('disabled', false),
      rows: number('rows', 5),
      maxRows: number('maxRows', 10)
    };
    return <TextArea {...props} />;
  })

  .add(
    'Метаданные',
    form(() => {
      return {
        placeholder: text('placeholder', TextAreaJson.placeholder),
        disabled: boolean('disabled', TextAreaJson.disabled),
        disabled: boolean('disabled', TextAreaJson.disabled),
        rows: number('rows', TextAreaJson.rows),
        maxRows: number('maxRows', TextAreaJson.maxRows)
      };
    })
  );
