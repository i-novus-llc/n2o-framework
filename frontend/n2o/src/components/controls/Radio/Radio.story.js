import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';

import { StateDecorator, Store } from '@sambego/storybook-state';

import withTests from 'N2oStorybook/withTests';

import Radio from './Radio';

const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const stories = storiesOf('Контролы/Радио', module);

stories.addDecorator(withTests('Checkbox'));

stories.addDecorator(StateDecorator(store));

stories.add(
  'Радио',
  () => {
    const props = {
      value: 2,
      disabled: false,
      checked: store.get('checked'),
      label: 'Label',
    };

    return (
      <Radio
        {...props}
        checked={store}
        onChange={() => store.set({ checked: !store.get('checked') })}
      />
    );
  },
  {
    info: {
      text: `
    Компонент 'Radio'
    ~~~js
    import Radio from 'n2o/lib/components/controls/Radio/Radio';
    
    <Radio
        value={2}
        checked={checked}
        label="Label"
        onChange="onChange"
    />
    ~~~
    `,
    },
  }
);
