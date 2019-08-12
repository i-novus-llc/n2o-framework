import React from 'react';
import { storiesOf } from '@storybook/react';
import TextEditor from './TextEditor';
import TextEditorJson from './TextEditor.meta.json';
import withForm from 'N2oStorybook/decorators/withForm';
import Factory from '../../../core/factory/Factory';
const stories = storiesOf('Контролы/Редактор текста', module);

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
        disabled: TextEditorJson.disabled,
        visible: TextEditorJson.visible,
        className: TextEditorJson.className,
      };

      return props;
    })
  );
