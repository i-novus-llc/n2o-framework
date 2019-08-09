import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
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

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);
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
      placeholder: text('placeholder', 'Введите значение'),
      disabled: boolean('disabled', false),
      rows: number('rows', 5),
      maxRows: number('maxRows', 10),
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
        placeholder: text('placeholder', TextAreaJson.placeholder),
        disabled: boolean('disabled', TextAreaJson.disabled),
        rows: number('rows', TextAreaJson.rows),
        maxRows: number('maxRows', TextAreaJson.maxRows),
      };
    })
  );
