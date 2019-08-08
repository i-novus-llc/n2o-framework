import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import CheckboxButton from './CheckboxButton';

const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const stories = storiesOf('Контролы/Чекбокс', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Checkbox'));
stories.addDecorator(jsxDecorator);

stories.add('Кнопка чекбокс', () => {
  const props = {
    disabled: boolean('disabled', false),
    checked: boolean('checked', store.state.checked),
    label: text('label', 'Label'),
  };

  return (
    <CheckboxButton
      {...props}
      checked={store.get('checked')}
      onChange={() => store.set({ checked: !store.get('checked') })}
    />
  );
});
