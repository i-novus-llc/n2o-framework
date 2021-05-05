import React from 'react'
import { storiesOf } from '@storybook/react'

import Factory from '../../../core/factory/Factory'
import { SNIPPETS } from '../../../core/factory/factoryLevels'

import Text from './Text'

const stories = storiesOf('UI Компоненты/Текст', module)

stories
    .add('Компонент', () => <Text id="test" text="Some text..." />, {
        info: {
            text: `
      Компонент 'Текст'
      ~~~js
      import Text from 'n2o-framework/lib/components/snippets/Text/Text';
      
      <Text id="test" text="Some text..." />
      ~~~
      `,
        },
    })
    .add(
        'Создание через Factory',
        () => {
            const dt = {
                id: 'uniqId',
                src: 'Text',
                text: 'Text',
                format: null,
            }

            return (
                <>
                    <Factory level={SNIPPETS} id="uniqId" {...dt} />
                </>
            )
        },
        {
            info: {
                text: `
      Компонент 'Текст'
      ~~~js
      import Factory from 'n2o-framework/lib/core/factory/Factory';
      
      <Factory level={SNIPPETS} id="uniqid" {...textProps} />
      ~~~
      `,
            },
        },
    )
