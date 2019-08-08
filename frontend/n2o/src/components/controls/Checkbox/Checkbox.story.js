import React from 'react';
import { setAddon, storiesOf, forceReRender } from '@storybook/react';
import JSXAddon from 'storybook-addon-jsx';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';

import Checkbox from './Checkbox';
import Factory from '../../../core/factory/Factory';

const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const form = withForm({ src: 'Checkbox' });

setAddon(JSXAddon);

const stories = storiesOf('Контролы/Чекбокс', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Checkbox'));
stories.addParameters({
  info: {
    propTables: [Checkbox],
    propTablesExclude: [Factory],
  },
});

stories
  .addWithJSX('Чекбокс', () => {
    const props = {
      value: number('value', 2),
      disabled: boolean('disabled', false),
      checked: boolean('checked', store.get('checked')),
      label: text('label', 'Label'),
    };

    return (
      <Checkbox
        {...props}
        checked={store.get('checked')}
        onChange={() => store.set({ checked: !store.get('checked') })}
      />
    );
  })
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
