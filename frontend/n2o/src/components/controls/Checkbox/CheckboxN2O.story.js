import React from 'react';
import { forceReRender, storiesOf } from '@storybook/react';
import { StateDecorator, Store } from '@sambego/storybook-state';

import CheckboxN2O, { CheckboxN2O as CheckboxComponent } from './CheckboxN2O';

const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const stories = storiesOf('Контролы/Чекбокс', module);

stories.addDecorator(StateDecorator(store));

stories.addParameters({
  info: {
    propTables: [CheckboxComponent],
    propTablesExclude: [CheckboxN2O, 'State', 'Unknown'],
  },
});

stories.add(
  'N2O чекбокс',
  () => {
    const props = {
      value: 2,
      disabled: false,
      checked: store.get('checked'),
      label: 'Label',
      indeterminate: false,
    };

    return (
      <CheckboxN2O
        {...props}
        checked={store.get('checked')}
        onChange={() => store.set({ checked: !store.get('checked') })}
      />
    );
  },
  {
    info: {
      text: `
    Контрол 'CheckboxN2O'
    ~~~js
    import CheckboxN2O from 'n2o-framework/lib/components/controls/Checkbox/CheckboxN2O;
    
    <CheckboxN2O
        label="Label" 
        value={2}
        disabled={false}
        onChange={onChange} 
        checked={checked} />
    ~~~
    `,
    },
  }
);
