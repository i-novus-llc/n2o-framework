import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import { withState } from '@dump247/storybook-state';

import CheckboxN2O from './CheckboxN2O';

const stories = storiesOf('Контролы/Чекбокс', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Checkbox'));

stories.add(
  'N2O чекбокс',
  withState({ checked: false }, store => {
    const props = {
      value: number('value', 2),
      disabled: boolean('disabled', false),
      checked: store.state.checked,
      label: text('label', 'Label'),
      indeterminate: boolean('indeterminate', false),
    };

    return (
      <CheckboxN2O
        {...props}
        onChange={() => store.set({ checked: !store.state.checked })}
      />
    );
  })
);
