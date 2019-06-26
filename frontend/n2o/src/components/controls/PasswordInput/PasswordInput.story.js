import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';
import PasswordInput from './PasswordInput';
import PasswordInputJson from './PasswordInput.meta.json';

const stories = storiesOf('Контролы/Ввод пароля', module);

const form = withForm({ src: 'PasswordInput' });

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('PasswordInput'));

stories
  .add('Компонент', () => {
    const props = {
      value: text('value', ''),
      placeholder: text('placeholder', 'Введите значение'),
      disabled: boolean('disabled', false),
      length: number('length', 25),
      showPasswordBtn: boolean('showPasswordBtn', false),
    };

    return <PasswordInput {...props} />;
  })

  .add(
    'Метаданные',
    form(() => {
      const props = {
        placeholder: text('placeholder', PasswordInputJson.placeholder),
        disabled: boolean('disabled', PasswordInputJson.disabled),
        length: number('length', PasswordInputJson.length),
      };

      return props;
    })
  )
  .add(
    'Фича просмотрал введенного пароля',
    form(() => {
      const props = {
        placeholder: text('placeholder', PasswordInputJson.placeholder),
        disabled: boolean('disabled', PasswordInputJson.disabled),
        length: number('length', PasswordInputJson.length),
        showPasswordBtn: boolean(
          'showPasswordBtn',
          PasswordInputJson.showPasswordBtn
        ),
      };

      return props;
    })
  );
