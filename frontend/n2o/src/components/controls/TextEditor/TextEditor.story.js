import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, text, boolean, object, array, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import TextEditor from './TextEditor';
import TextEditorJson from './TextEditor.meta.json';
import withForm from 'N2oStorybook/decorators/withForm';
const stories = storiesOf('Контролы/Редактор текста', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CodeEditor'));

const form = withForm({ src: 'TextEditor' });

stories
  .addWithJSX('Компонент', () => {
    const props = {
      disabled: boolean('disabled', false),
      visible: boolean('visible', true),
      value: text('value', ''),
      onChange: action('text-editor-on-change')
    };

    return <TextEditor {...props} />;
  })

  .add(
    'Метаданные',
    form(() => {
      const props = {
        disabled: boolean('disabled', TextEditorJson.disabled),
        visible: boolean('visible', TextEditorJson.visible),
        value: text('value', TextEditorJson.value),
        name: text('name', TextEditorJson.name),
        className: text('className', TextEditorJson.className),
        toolbarConfig: object('toolbarConfig', TextEditorJson.toolbarConfig)
      };

      return props;
    })
  );
