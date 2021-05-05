import React from 'react'
import { storiesOf } from '@storybook/react'
import withPage from 'N2oStorybook/decorators/withPage'
import fetchMock from 'fetch-mock'

import Form from '../../../../.storybook/json/formWidgetListFiltering'
import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'

fetchMock
    .restore()
    .get('begin:n2o/data/getFiltered?type_eq=1', () => ({
        list: [{ id: 1, name: 'test1' }, { id: 2, name: 'test2' }],
        count: 2,
        size: 30,
        page: 1,
    }))
    .get('begin:n2o/data/getFiltered?type_eq=2', () => ({
        list: [{ id: 3, name: 'test3' }, { id: 4, name: 'test4' }],
        count: 2,
        size: 30,
        page: 1,
    }))
    .get('begin:n2o/data/getFiltered?type_eq=0', () => ({ list: [], count: 0, size: 30, page: 1 }))
    .get('begin:n2o/data/getFiltered', () => ({
        list: [
            { id: 1, name: 'test1222' },
            { id: 2, name: 'test2' },
            { id: 3, name: 'test3' },
            { id: 4, name: 'test4' },
        ],
        count: 4,
        size: 30,
        page: 1,
    }))

const stories = storiesOf('Виджеты/Форма', module)

const renderForm = json => (
    <Factory level={WIDGETS} {...json.Page_Form} id="Page_Form" />
)

stories.add('Фильтрация списковых полей', () => withPage(Form, 'Page_Form')(() => renderForm(Form)))
