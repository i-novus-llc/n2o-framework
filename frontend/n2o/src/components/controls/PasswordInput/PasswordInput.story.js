import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';
import PasswordInput, {
  PasswordInput as PasswordInputComponent,
} from './PasswordInput';
import PasswordInputJson from './PasswordInput.meta.json';
import Factory from '../../../core/factory/Factory';

const store = new Store({
  value: '',
});

store.subscribe(forceReRender);

const stories = storiesOf('Контролы/Ввод пароля', module);

const form = withForm({ src: 'PasswordInput' });

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('PasswordInput'));
stories.addDecorator(jsxDecorator);
stories.addDecorator(StateDecorator(store));
stories.addParameters({
  info: {
    propTables: [PasswordInputComponent],
    propTablesExclude: [PasswordInput, Factory],
  },
});

stories
  .add('Компонент', () => {
    const props = {
      placeholder: text('placeholder', 'Введите значение'),
      disabled: boolean('disabled', false),
      length: number('length', 25),
      showPasswordBtn: boolean('showPasswordBtn', false),
    };

    return (
      <PasswordInput
        {...props}
        value={store.get('value')}
        onChange={event => store.set({ value: event.target.value })}
      />
    );
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
