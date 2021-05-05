import React from 'react'
import { storiesOf } from '@storybook/react'

import RadioButton from '../Radio/RadioButton'

import RadioGroup from './RadioGroup'

const stories = storiesOf('Контролы/Радио', module)

stories.add(
    'Группа в виде кнопок',
    () => {
        const props = {
            value: '2',
            disabled: false,
            visible: true,
            className: '',
            inline: true,
        }

        return (
            <RadioGroup
                name="numbers"
                isBtnGroup
                onChange={() => {}}
                {...props}
            >
                <RadioButton value="1" label="Первый" />
                <RadioButton value="2" label="Второй" />
                <RadioButton value="3" label="Третий" />
            </RadioGroup>
        )
    },
    {
        info: {
            text: `
    Компонент 'RadioN2O'
    ~~~js
    import RadioGroup from 'n2o-framework/lib/components/controls/RadioGroup/RadioGroup';
    import RadioButton from 'n2o-framework/lib/components/controls/Radio/RadioButton';
    
    <RadioGroup 
        name="numbers"
        isBtnGroup={true} 
        onChange={onChange}
        value="2"
        inline={true}
      >
      <RadioButton value="1" label="Первый" />
      <RadioButton value="2" label="Второй" />
      <RadioButton value="3" label="Третий" />
    </RadioGroup>
    ~~~
    `,
        },
    },
)
