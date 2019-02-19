import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean } from '@storybook/addon-knobs/react';

import TextEditor from './TextEditor';
import TextEditorJson from './TextEditor.meta.json';
import withForm from 'N2oStorybook/decorators/withForm';
const stories = storiesOf('Контролы/Редактор текста', module);

stories.addDecorator(withKnobs);

const form = withForm({ src: 'TextEditor' });

stories
  .add('Компонент', () => {
    return <TextEditor />;
  })
  .add(
    'Метаданные',
    form(() => {
      const props = {
        disabled: boolean('disabled', TextEditorJson.disabled),
        visible: boolean('visible', TextEditorJson.visible),
        className: text('className', TextEditorJson.className)
      };

      return props;
    })
  );
