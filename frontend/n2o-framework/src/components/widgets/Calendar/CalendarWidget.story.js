import React from 'react'
import { storiesOf } from '@storybook/react'
import fetchMock from 'fetch-mock'
import omit from 'lodash/omit'

import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'

import metadata from './CalendarWidget.meta.json'
import Calendar from './Calendar'

const stories = storiesOf('Виджеты/Виджет Календарь', module)
stories.addParameters({
    info: {
        propTables: [Calendar],
        propTablesExclude: [Factory],
    },
})
const urlPattern = '*'

stories
    .add('Компонент', () => (
        <Factory
            level={WIDGETS}
            {...metadata.Page_Calendar}
            id="Page_Calendar"
        />
    ))
    .add('События', () => {
        fetchMock.restore().get(urlPattern, url => ({
            meta: {},
            page: 0,
            size: 10,
            list: [
                {
                    id: 1,
                    begin: '2020-06-10T10:00:00',
                    end: '2020-06-10T15:00:00',
                    name: 'Some title',
                    tooltip: 'tooltip text',
                    color: 'red',
                    disabled: true,
                },
            ],
        }))

        const withDataProvider = {
            dataProvider: {
                url: 'n2o/data/test',
                pathMapping: {},
                queryMapping: {
                    'filter.name': {
                        link: 'models.filter[\'Page_Table\'].name',
                    },
                    'filter.surname': {
                        link: 'models.filter[\'Page_Table\'].surname',
                    },
                },
            },
            paging: {
                size: 10,
            },
            ...metadata.Page_Calendar,
        }

        return <Factory level={WIDGETS} {...withDataProvider} id="Page_Calendar" />
    })
    .add('Планирование ресурсов', () => {
        const resources = {
            calendar: {
                defaultView: 'day',
                views: ['day', 'week'],
                resources: [
                    {
                        id: 1,
                        title: 'Конференц зал',
                    },
                    {
                        id: 2,
                        title: 'Вторая Эрпэльная',
                    },
                ],
            },
        }

        const withResources = {
            ...omit(metadata.Page_Calendar, ['calendar.views']),
            ...resources,
        }

        return <Factory level={WIDGETS} {...withResources} id="Page_Calendar" />
    })
