import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, boolean } from '@storybook/addon-knobs/react';
import { withState } from '@dump247/storybook-state';
import withForm from 'N2oStorybook/decorators/withForm';
import Switch from './Switch';
import SwitchJson from './Switch.meta.json';

const stories = storiesOf('Контролы/Переключатель', module);
const form = withForm({ src: SwitchJson.src });

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);

stories
  .add(
    'Компонент',
    withState({ checked: false }, store => {
      const props = {
        disabled: boolean('disabled', false),
        checked: boolean('checked', store.state.checked),
      };

      return (
        <Switch
          {...props}
          onChange={() => store.set({ checked: !store.state.checked })}
        />
      );
    })
  )
  .add('Метаданные', form(() => ({})));
