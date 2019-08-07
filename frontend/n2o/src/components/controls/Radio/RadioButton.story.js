import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import { withState } from '@dump247/storybook-state';

import RadioButton from './RadioButton';

const stories = storiesOf('Контролы/Радио', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Checkbox'));
stories.addDecorator(jsxDecorator);

stories.add(
  'Кнопка радио',
  withState({ checked: false }, store => {
    const props = {
      value: number('value', 2),
      disabled: boolean('disabled', false),
      checked: boolean('checked', store.state.checked),
      label: text('label', 'Label'),
    };

    return (
      <RadioButton
        {...props}
        onChange={() => store.set({ checked: !store.state.checked })}
      />
    );
  })
);
