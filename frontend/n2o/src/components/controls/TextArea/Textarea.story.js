import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { StateDecorator, Store } from '@sambego/storybook-state';

import withForm from 'N2oStorybook/decorators/withForm';
import TextArea from './TextArea';
import TextAreaJson from './TextArea.meta.json';
import Factory from '../../../core/factory/Factory';

const store = new Store({
  value: '',
});

store.subscribe(forceReRender);

const form = withForm({ src: 'TextArea' });

const stories = storiesOf('Контролы/Многострочное текстовое поле', module);

stories.addDecorator(StateDecorator(store));
stories.addParameters({
  info: {
    propTables: [TextArea],
    propTablesExclude: [Factory],
  },
});

stories
  .add('Компонент', () => {
    const props = {
      placeholder: 'Введите значение',
      disabled: false,
      rows: 5,
      maxRows: 10,
    };
    return (
      <TextArea
        {...props}
        onChange={e => store.set({ value: e.target.value })}
        value={store.get('value')}
      />
    );
  })

  .add(
    'Метаданные',
    form(() => {
      return {
        placeholder: TextAreaJson.placeholder,
        disabled: TextAreaJson.disabled,
        rows: TextAreaJson.rows,
        maxRows: TextAreaJson.maxRows,
      };
    })
  );
