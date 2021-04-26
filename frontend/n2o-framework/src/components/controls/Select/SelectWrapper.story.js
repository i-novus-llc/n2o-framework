import React from 'react'
import { storiesOf } from '@storybook/react'
import fetchMock from 'fetch-mock'
import { parseUrl, getStubData } from 'N2oStorybook/fetchMock'
import withForm from 'N2oStorybook/decorators/withForm'

import SelectWrapper from './SelectWrapper'
import SelectJson from './SelectWrapper.meta.json'

const form = withForm({ src: 'N2OSelect' })
const stories = storiesOf('Контролы/Выпадающий список', module)

stories.add(
    'Метаданные',
    form(() => {
        const props = {
            value: SelectJson.value,
            visible: SelectJson.visible,
            valueFieldId: SelectJson.valueFieldId,
            labelFieldId: SelectJson.labelFieldId,
            queryId: SelectJson.queryId,
            size: SelectJson.size,
            heightSize: SelectJson.heightSize,
            disabled: SelectJson.disabled,
            autoFocus: SelectJson.autoFocus,
            required: SelectJson.required,
            dataProvider: SelectJson.dataProvider,
        }

        const list = [
            {
                id: '1',
                label: 'Alex',
            },
            {
                id: '2',
                label: 'lex',
            },
            {
                id: '3',
                label: 'ex',
            },
        ]

        fetchMock.restore().get('begin:n2o/data', { list })

        return props
    }),
)
