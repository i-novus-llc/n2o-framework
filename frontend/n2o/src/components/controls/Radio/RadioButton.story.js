import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import RadioButton from './RadioButton';

const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const stories = storiesOf('Контролы/Радио', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Checkbox'));
stories.addDecorator(jsxDecorator);
stories.addDecorator(StateDecorator(store));

stories.add('Кнопка радио', () => {
  const props = {
    value: number('value', 2),
    disabled: boolean('disabled', false),
    checked: boolean('checked', store.state.checked),
    label: text('label', 'Label'),
  };

  return (
    <RadioButton
      {...props}
      checked={store.get('checked')}
      onChange={() => store.set({ checked: !store.state.checked })}
    />
  );
});
