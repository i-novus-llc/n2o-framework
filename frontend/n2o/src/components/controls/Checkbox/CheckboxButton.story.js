import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { StateDecorator, Store } from '@sambego/storybook-state';

import withTests from 'N2oStorybook/withTests';

import CheckboxButton from './CheckboxButton';

const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const stories = storiesOf('Контролы/Чекбокс', module);

stories.addDecorator(withTests('Checkbox'));

stories.add('Кнопка чекбокс', () => {
  const props = {
    disabled: false,
    checked: store.state.checked,
    label: 'Label',
  };

  return (
    <CheckboxButton
      {...props}
      checked={store.get('checked')}
      onChange={() => store.set({ checked: !store.get('checked') })}
    />
  );
});
