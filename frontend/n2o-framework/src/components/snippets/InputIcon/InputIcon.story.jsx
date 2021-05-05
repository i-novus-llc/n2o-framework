import React from 'react'
import { storiesOf } from '@storybook/react'

import InputIcon from './InputIcon'

const stories = storiesOf('UI Компоненты/Иконка для инпутов', module)

stories.add(
    'Компонент',
    () => (
        <InputIcon clickable hoverable>
            <span className="fa fa-chevron-down" />
        </InputIcon>
    ),
    {
        info: {
            text: `
      Компонент 'Иконка для инпутов'
      ~~~js
      import InputText from 'n2o-framework/lib/components/snippets/InputIcon/InputIcon';
      
      <InputIcon>
        <span className="fa fa-chevron-down" />
      </InputIcon>
      ~~~
      `,
        },
    },
)
