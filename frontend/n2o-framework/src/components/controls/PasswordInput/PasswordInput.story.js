import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';

import { StateDecorator, Store } from '@sambego/storybook-state';
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

stories.addDecorator(StateDecorator(store));
stories.addParameters({
  info: {
    propTables: [PasswordInputComponent],
    propTablesExclude: [PasswordInput, Factory],
  },
});

stories
  .add(
    'Компонент',
    () => {
      const props = {
        placeholder: 'Введите значение',
        disabled: false,
        length: 25,
        showPasswordBtn: false,
      };

      return (
        <PasswordInput
          {...props}
          value={store.get('value')}
          onChange={event => store.set({ value: event.target.value })}
        />
      );
    },
    {
      info: {
        text: `
      Компонент 'Ввод пароля'
      ~~~js
      import PasswordInput from 'n2o-framework/lib/components/controls/PasswordInput/PasswordInput';
      
      <PasswordInput
          placeholder="Введите значение"
          length={25}
          showPasswordBtn={false}
          value={value}
          onChange={onChange}
      />
      ~~~
      `,
      },
    }
  )

  .add(
    'Метаданные',
    form(() => {
      const props = {
        placeholder: PasswordInputJson.placeholder,
        disabled: PasswordInputJson.disabled,
        length: PasswordInputJson.length,
      };

      return props;
    })
  )
  .add(
    'Фича просмотра введенного пароля',
    form(() => {
      const props = {
        placeholder: PasswordInputJson.placeholder,
        disabled: PasswordInputJson.disabled,
        length: PasswordInputJson.length,
        showPasswordBtn: PasswordInputJson.showPasswordBtn,
      };

      return props;
    })
  );
