import React from 'react'
import { storiesOf } from '@storybook/react'
import withForm from 'N2oStorybook/decorators/withForm'

import Factory from '../../../core/factory/Factory'

import TextEditor from './TextEditor'
import TextEditorJson from './TextEditor.meta.json'

const stories = storiesOf('Контролы/Редактор текста', module)

stories.addParameters({
    info: {
        propTables: [TextEditor],
        propTablesExclude: [Factory],
    },
})

const form = withForm({ src: 'TextEditor' })

stories
    .add(
        'Компонент',
        () => <TextEditor />,
        {
            info: {
                text: `
      Компонент 'Редактор текста'
      ~~~js
      import TextEditor from 'n2o-framework/lib/components/controls/TextEditor/TextEditor';
      
      <TextEditor />
      ~~~
      `,
            },
        },
    )
    .add(
        'Кастомный тулбар',
        () => <TextEditor toolbarConfig={TextEditorJson.toolbarConfig} />,
        {
            info: {
                text: `
      Компонент 'Редактор текста'
      ~~~js
      import TextEditor from 'n2o-framework/lib/components/controls/TextEditor/TextEditor';
      
      <TextEditor
          toolbarConfig={{
            "inline": {
              "visible": true,
              "inDropdown": false,
              "bold": { "visible": true },
              "italic": { "visible": true }
            },
            "list": {
              "visible": true,
              "inDropdown": true
            },
            "textAlign": {
              "visible": true,
              "inDropdown": true
            }
          }}
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Метаданные',
        form(() => {
            const props = {
                disabled: TextEditorJson.disabled,
                visible: TextEditorJson.visible,
                className: TextEditorJson.className,
            }

            return props
        }),
    )
