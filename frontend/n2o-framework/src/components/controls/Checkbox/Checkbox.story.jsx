import React from 'react'
import { setAddon, storiesOf, forceReRender } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'
import withForm from 'N2oStorybook/decorators/withForm'

import Factory from '../../../core/factory/Factory'

import Checkbox from './Checkbox'

const store = new Store({
    checked: false,
})

store.subscribe(forceReRender)

const form = withForm({ src: 'Checkbox' })
const stories = storiesOf('Контролы/Чекбокс', module)

stories.addParameters({
    info: {
        propTables: [Checkbox],
        propTablesExclude: [Factory],
    },
})

stories
    .add(
        'Чекбокс',
        () => {
            const props = {
                value: 2,
                disabled: false,
                checked: store.get('checked'),
                label: 'Label',
            }

            return (
                <Checkbox
                    {...props}
                    checked={store.get('checked')}
                    onChange={() => store.set({ checked: !store.get('checked') })}
                />
            )
        },
        {
            info: {
                text: `
      Компонент 'Checkbox'
      ~~~js
      import Checkbox from 'n2o-framework/lib/components/controls/Checkbox/Checkbox';
      
      <Checkbox
        value={2}
        label="Label"
        checked={checked}
        onChange={onChange}
      />
      ~~~
      `,
            },
        },
    )
    .add(
        'Метаданные',
        form(() => ({
            disabled: false,
            label: 'Label',
        })),
    )
