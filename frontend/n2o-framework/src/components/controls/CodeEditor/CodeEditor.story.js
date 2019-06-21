import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import {
  withKnobs,
  text,
  boolean,
  number,
  select,
} from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';
import CodeEditor from './CodeEditor';
import CodeEditorJson from './CodeEditor.meta.json';

const form = withForm({ src: 'CodeEditor' });

const stories = storiesOf('Контролы/Редактор кода', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CodeEditor'));

stories
  .add('Компонент', () => {
    const props = {
      disabled: boolean('disabled', false),
      visible: boolean('visible', true),
      lang: select(
        'lang',
        ['javascript', 'xml', 'sql', 'groovy', 'java', 'html'],
        'java'
      ),
      minLines: number('minLines', 5),
      maxLines: number('maxLines', 30),
      autocomplete: boolean('autocomplete', true),
      value: text('value', ''),
      onChange: action('code-editor-on-change'),
    };

    return <CodeEditor {...props} />;
  })

  .add(
    'Метаданные',
    form(() => {
      const props = {
        disabled: boolean('disabled', CodeEditorJson.disabled),
        lang: select(
          'lang',
          ['javascript', 'xml', 'sql', 'groovy', 'java', 'html'],
          CodeEditorJson.lang
        ),
        minLines: number('minLines', CodeEditorJson.micro),
        maxLines: number('maxLines', CodeEditorJson.maxLines),
        autocomplete: boolean('autocomplete', CodeEditorJson.autocomplete),
        value: text('value', CodeEditorJson.value),
        onChange: action('code-editor-on-change'),
      };

      return props;
    })
  );
