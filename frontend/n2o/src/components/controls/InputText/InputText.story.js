import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';
import Input from './InputText';
import InputJson from './InputText.meta.json';
import Factory from '../../../core/factory/Factory';

const store = new Store({
  value: '',
});

store.subscribe(forceReRender);

const form = withForm({ src: 'InputText' });

const stories = storiesOf('Контролы/Ввод текста', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Input'));
stories.addDecorator(jsxDecorator);
stories.addDecorator(StateDecorator(store));
stories.addParameters({
  info: {
    propTables: [Input],
    propTablesExclude: [Factory],
  },
});

stories
  .add('Компонент', () => (
    <Input
      value={store.get('value')}
      onChange={value => store.set({ value })}
    />
  ))
  .add(
    'Метаданные',
    form(() => {
      const props = {
        placeholder: text('placeholder', InputJson.placeholder),
        disabled: boolean('disabled', InputJson.disabled),
        length: number('length', InputJson.length),
      };

      return props;
    })
  );
