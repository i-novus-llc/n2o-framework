import React from 'react'
import { storiesOf } from '@storybook/react'

import Checkbox from '../Checkbox/Checkbox'

import CheckboxGroup from './CheckboxGroup'

const stories = storiesOf('Контролы/Группа чекбоксов', module)

stories.add(
    'Компонент',
    () => {
        const props = {
            value: ['1', '2'],
            disabled: false,
            visible: true,
            className: '',
            inline: false,
        }

        return (
            <CheckboxGroup name="numbers" {...props}>
                <Checkbox value="1" label="Первый" />
                <Checkbox value="2" label="Второй" />
                <Checkbox value="3" label="Третий" />
            </CheckboxGroup>
        )
    },
    {
        info: {
            text: `
    Компонент 'CheckboxGroup'
    ~~~js
    import CheckboxGroup from 'n2o-framework/lib/components/controls/CheckboxGroup/CheckboxGroup';
    
    <CheckboxGroup 
        name="numbers"
        value
      >
      <Checkbox value="1" label="Первый" />
      <Checkbox value="2" label="Второй" />
      <Checkbox value="3" label="Третий" />
    </CheckboxGroup>
    ~~~
    `,
        },
    },
)
