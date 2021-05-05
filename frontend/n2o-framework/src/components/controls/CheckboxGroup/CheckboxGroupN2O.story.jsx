import React from 'react'
import { storiesOf } from '@storybook/react'

import CheckboxAlt, {
    CheckboxN2O as CheckboxComponent,
} from '../Checkbox/CheckboxN2O'

import CheckboxGroup from './CheckboxGroup'

const stories = storiesOf('Контролы/Группа чекбоксов', module)

stories.addParameters({
    info: {
        propTables: [CheckboxComponent],
        propTablesExclude: [CheckboxAlt],
    },
})

stories.add(
    'N2O группа',
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
                <CheckboxAlt value="1" label="Первый" />
                <CheckboxAlt value="2" label="Второй" />
                <CheckboxAlt value="3" label="Третий" />
            </CheckboxGroup>
        )
    },
    {
        info: {
            text: `
    Компонент 'CheckboxGroupN2O'
    ~~~js
    import CheckboxGroup from 'n2o-framework/lib/components/controls/CheckboxGroup/CheckboxGroup';
    import CheckboxAlt from 'n2o-framework/lib/components/controls/Checkbox/CheckboxN2O';
    
    <CheckboxGroup name="numbers" value={['1', '2']} visible={true}>
      <CheckboxAlt value="1" label="Первый" />
      <CheckboxAlt value="2" label="Второй" />
      <CheckboxAlt value="3" label="Третий" />
    </CheckboxGroup>
    ~~~
    `,
        },
    },
)
