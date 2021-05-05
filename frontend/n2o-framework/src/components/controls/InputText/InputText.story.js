import React from 'react'
import { storiesOf, forceReRender } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'
import withForm from 'N2oStorybook/decorators/withForm'

import InputIcon from '../../snippets/InputIcon/InputIcon'
import Factory from '../../../core/factory/Factory'

import Input from './InputText'
import InputJson from './InputText.meta.json'

const store = new Store({
    value: '',
})

store.subscribe(forceReRender)

const form = withForm({ src: 'InputText' })

const stories = storiesOf('Контролы/Ввод текста', module)

stories.addDecorator(StateDecorator(store))
stories.addParameters({
    info: {
        propTables: [Input],
        propTablesExclude: [Factory],
    },
})

stories
    .add(
        'Компонент',
        () => (
            <Input
                disabled
                value={store.get('value')}
                onChange={value => store.set({ value })}
            />
        ),
        {
            info: {
                text: `
      Компонент 'Ввод текста'
      ~~~js
      import InputText from 'n2o-framework/lib/components/controls/InputText/InputText';
      
      <InputText value={value} onChange={onChange} />
      ~~~
      `,
            },
        },
    )
    .add(
        'Метаданные',
        form(() => ({
            placeholder: InputJson.placeholder,
            disabled: InputJson.disabled,
            length: InputJson.length,
        })),
    )
    .add('Префикс / суффикс', () => (
        <Input
            prefix={<span className="fa fa-dollar" />}
            suffix={(
                <InputIcon clickable hoverable>
                    <span className="fa fa-remove" />
                </InputIcon>
            )}
            value={store.get('value')}
            onChange={value => store.set({ value })}
        />
    ))
