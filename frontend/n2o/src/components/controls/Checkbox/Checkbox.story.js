import React from 'react';
import { setAddon, storiesOf } from '@storybook/react';
import JSXAddon from 'storybook-addon-jsx';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import { withState } from '@dump247/storybook-state';
import withForm from 'N2oStorybook/decorators/withForm';

import Checkbox from './Checkbox';

const form = withForm({ src: 'Checkbox' });

setAddon(JSXAddon);

const stories = storiesOf('Контролы/Чекбокс', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Checkbox'));

stories
  .addWithJSX(
    'Чекбокс',
    withState({ checked: false }, store => {
      const props = {
        value: number('value', 2),
        disabled: boolean('disabled', false),
        checked: boolean('checked', store.state.checked),
        label: text('label', 'Label'),
      };

      return (
        <Checkbox
          {...props}
          onChange={() => store.set({ checked: !store.state.checked })}
        />
      );
    }),
    { jsx: { skip: 1 } }
  )
  .add(
    'Метаданные',
    form(() => {
      const props = {
        disabled: boolean('disabled', false),
        label: text('label', 'Label'),
      };

      return props;
    })
  );
