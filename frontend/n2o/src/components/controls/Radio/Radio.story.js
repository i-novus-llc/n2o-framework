import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import Radio from './Radio';

const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const stories = storiesOf('Контролы/Радио', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Checkbox'));
stories.addDecorator(jsxDecorator);
stories.addDecorator(StateDecorator(store));

stories.add('Радио', () => {
  const props = {
    value: number('value', 2),
    disabled: boolean('disabled', false),
    checked: boolean('checked', store.get('checked')),
    label: text('label', 'Label'),
  };

  return (
    <Radio
      {...props}
      checked={store}
      onChange={() => store.set({ checked: !store.get('checked') })}
    />
  );
});
