import React from 'react'
import { storiesOf } from '@storybook/react'
import withForm from 'N2oStorybook/decorators/withForm'

import Factory from '../../../core/factory/Factory'

import CodeEditor from './CodeEditor'
import CodeEditorJson from './CodeEditor.meta.json'

const form = withForm({ src: 'CodeEditor' })

const stories = storiesOf('Контролы/Редактор кода', module)

stories.addParameters({
    info: {
        propTables: [CodeEditor],
        propTablesExclude: [Factory],
    },
})

stories
    .add(
        'Компонент',
        () => {
            const props = {
                disabled: false,
                visible: true,
                lang: 'java',
                minLines: 5,
                maxLines: 30,
                autocomplete: true,
                value: '',
            }

            return <CodeEditor {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Редактор кода'
      ~~~js
      import CodeEditor from 'n2o-framework/lib/components/controls/CodeEditor/CodeEditor';

      <CodeEditor
          lang="java"
          minLines={5}
          maxLines={30}
          autocomplete={true}
          value=""
       />
      ~~~
      `,
            },
        },
    )

    .add(
        'Метаданные',
        form(() => ({
            disabled: CodeEditorJson.disabled,
            lang: CodeEditorJson.lang,
            minLines: CodeEditorJson.micro,
            maxLines: CodeEditorJson.maxLines,
            autocomplete: CodeEditorJson.autocomplete,
            value: CodeEditorJson.value,
        })),
    )
