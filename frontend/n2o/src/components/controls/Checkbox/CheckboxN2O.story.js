import React from 'react';
import { forceReRender, storiesOf } from '@storybook/react';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import CheckboxN2O, { CheckboxN2O as CheckboxComponent } from './CheckboxN2O';

const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const stories = storiesOf('Контролы/Чекбокс', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Checkbox'));
stories.addDecorator(jsxDecorator);
stories.addDecorator(StateDecorator(store));

stories.addParameters({
  info: {
    propTables: [CheckboxComponent],
    propTablesExclude: [CheckboxN2O],
  },
});

stories.add('N2O чекбокс', () => {
  const props = {
    value: number('value', 2),
    disabled: boolean('disabled', false),
    checked: store.get('checked'),
    label: text('label', 'Label'),
    indeterminate: boolean('indeterminate', false),
  };

  return (
    <CheckboxN2O
      {...props}
      checked={store.get('checked')}
      onChange={() => store.set({ checked: !store.get('checked') })}
    />
  );
});
