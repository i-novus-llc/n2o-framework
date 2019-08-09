import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean } from '@storybook/addon-knobs/react';

import TextEditor from './TextEditor';
import TextEditorJson from './TextEditor.meta.json';
import withForm from 'N2oStorybook/decorators/withForm';
import Factory from '../../../core/factory/Factory';
const stories = storiesOf('Контролы/Редактор текста', module);

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);
stories.addParameters({
  info: {
    propTables: [TextEditor],
    propTablesExclude: [Factory],
  },
});

const form = withForm({ src: 'TextEditor' });

stories
  .add('Компонент', () => {
    return <TextEditor />;
  })
  .add('Кастомный тулбар', () => {
    return <TextEditor toolbarConfig={TextEditorJson.toolbarConfig} />;
  })
  .add(
    'Метаданные',
    form(() => {
      const props = {
        disabled: boolean('disabled', TextEditorJson.disabled),
        visible: boolean('visible', TextEditorJson.visible),
        className: text('className', TextEditorJson.className),
      };

      return props;
    })
  );
