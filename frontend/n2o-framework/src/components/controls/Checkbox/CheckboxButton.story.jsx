import React from 'react'
import { storiesOf, forceReRender } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'

import CheckboxButton from './CheckboxButton'

const store = new Store({
    checked: false,
})

store.subscribe(forceReRender)

const stories = storiesOf('Контролы/Чекбокс', module)

stories.add(
    'Кнопка чекбокс',
    () => {
        const props = {
            disabled: false,
            checked: store.state.checked,
            label: 'Label',
        }

        return (
            <CheckboxButton
                {...props}
                checked={store.get('checked')}
                onChange={() => store.set({ checked: !store.get('checked') })}
            />
        )
    },
    {
        info: {
            text: `
      Компонент 'CheckboxButton'
      ~~~js
      import CheckboxButton from 'n2o-framework/lib/components/controls/Checkbox/CheckboxButton';
      
      <CheckboxButton
        label="Label"
        checked={checked}
        onChange={onChange}
      />
      ~~~
      `,
        },
    },
)
