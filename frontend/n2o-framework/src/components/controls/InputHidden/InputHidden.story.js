import React from 'react'
import { storiesOf } from '@storybook/react'

import Input from './InputHidden'
import InputHiddenMeta from './InputHidden.meta'

const stories = storiesOf('Контролы/Скрытое поле', module)

stories
    .add(
        'Компонент',
        () => {
            const props = {
                value: 'InputHidden value',
            }

            return (
                <div>
                    <p>Здесь находиться скрытое поле</p>
                    <Input {...props} />
                </div>
            )
        },
        {
            info: {
                text: `
      Компонент 'Скрытое поле'
      ~~~js
      import InputHidden from 'n2o-framework/lib/components/controls/InputHidden/InputHidden';
      
      <InputHidden value="InputHidden value" />
      ~~~
      `,
            },
        },
    )
    .add(
        'Метаданные',
        () => (
            <div>
                <p>Здесь находиться скрытое поле</p>
                <Input {...InputHiddenMeta} />
            </div>
        ),
        {
            info: {
                text: `
      Компонент 'Скрытое поле'
      ~~~js
      import InputHidden from 'n2o-framework/lib/components/controls/InputHidden/InputHidden';
      
      <InputHidden name="InputHidden" value="value" />
      ~~~
      `,
            },
        },
    )
