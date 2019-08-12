import React from 'react';
import { setAddon, storiesOf, forceReRender } from '@storybook/react';
import { StateDecorator, Store } from '@sambego/storybook-state';

import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';

import Checkbox from './Checkbox';
import Factory from '../../../core/factory/Factory';
const store = new Store({
  checked: false,
});

store.subscribe(forceReRender);

const form = withForm({ src: 'Checkbox' });
const stories = storiesOf('Контролы/Чекбокс', module);

stories.addDecorator(withTests('Checkbox'));
stories.addParameters({
  info: {
    propTables: [Checkbox],
    propTablesExclude: [Factory],
  },
});

stories
  .add('Чекбокс', () => {
    const props = {
      value: 2,
      disabled: false,
      checked: store.get('checked'),
      label: 'Label',
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
        disabled: false,
        label: 'Label',
      };

      return props;
    })
  );
