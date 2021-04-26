import React from 'react'
import { storiesOf } from '@storybook/react'

import InputNumberInterval from './InputNumberInterval'

const stories = storiesOf('Контролы/Интервал ввода чисел', module)

stories.add(
    'базовый функционал',
    () => {
        const props = {
            value: [1, 1],
            visible: true,
            step: '0.1',
            min: -0.5,
            max: 1.5,
            showButtons: true,
            disabled: false,
        }

        return <InputNumberInterval {...props} />
    },
    {
        info: {
            text: `
    Компонент 'Интервал чисел'
    ~~~js
    import InputNumberInterval from 'n2o-framework/lib/components/controls/InputNumber/InputNumberInterval';
    
    <InputNumberInterval 
        value={[1, 1]}
        visible={true}
        step="0.1"
        min={-0.5}
        max={1.5}
        showButtons={true}
        disabled={false}
    />
    ~~~
    `,
        },
    },
)
