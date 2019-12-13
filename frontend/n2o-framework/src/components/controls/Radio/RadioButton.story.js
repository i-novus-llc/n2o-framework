import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';

import { StateDecorator, Store } from '@sambego/storybook-state';

import RadioButton from './RadioButton';

const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const stories = storiesOf('Контролы/Радио', module);

stories.addDecorator(StateDecorator(store));

stories.add(
  'Кнопка радио',
  () => {
    const props = {
      value: 2,
      disabled: false,
      checked: store.state.checked,
      label: 'Label',
    };

    return (
      <RadioButton
        {...props}
        checked={store.get('checked')}
        onChange={() => store.set({ checked: !store.state.checked })}
      />
    );
  },
  {
    info: {
      text: `
    Компонент 'RadioButton'
    ~~~js
    import RadioButton from 'n2o-framework/lib/components/controls/Radio/RadioButton';
    
    <RadioButton
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
