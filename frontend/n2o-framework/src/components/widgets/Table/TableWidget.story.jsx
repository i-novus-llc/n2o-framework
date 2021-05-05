import React from 'react'
import { storiesOf } from '@storybook/react'
import { getStubData, page } from 'N2oStorybook/fetchMock'
import { filterMetadata, newEntry, tableActions } from 'N2oStorybook/json'
import fetchMock from 'fetch-mock'
import set from 'lodash/set'

import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'
import { START_INVOKE } from '../../../constants/actionImpls'
import { id } from '../../../utils/id'
import cloneObject from '../../../utils/cloneObject'
import AuthButtonContainer from '../../../core/auth/AuthLogin'
import withPage from '../../../../.storybook/decorators/withPage'

import metadata from './TableWidget.meta.json'

const stories = storiesOf('Виджеты/Таблица', module)

const urlPattern = 'begin:n2o/data'

stories
    .addDecorator(withPage(metadata))
    .add('Метаданные', () => {
        fetchMock.restore().get(urlPattern, url => getStubData(url))

        return (
            <Factory level={WIDGETS} {...metadata.Page_Table} id="Page_Table" />
        )
    })
    .add('Создание записи', () => {
        let response = null

        const getStub = (url) => {
            if (response) {
                const sData = getStubData(url)

                sData.list.unshift(response)

                return sData
            }

            return getStubData(url)
        }

        const postStub = (url, xhr) => {
            response = { id: id(), ...JSON.parse(xhr.body) }

            return {
                count: 201,
                size: 1,
                page: 1,
                message: {
                    severity: 'success',
                    text: 'Запись успешно добавлена!',
                },
                list: [response],
            }
        }

        fetchMock
            .restore()
            .get(urlPattern, url => getStub(url))
            .post(urlPattern, (url, xhr) => postStub(url, xhr))

        return (
            <Factory level={WIDGETS} {...newEntry.Page_Table} id="Page_Table" />
        )
    })
    .add('Сообщения', () => {
        const toolbar = {
            topLeft: [
                {
                    buttons: [
                        {
                            id: '1',
                            title: 'Invoke c алертом',
                            actionId: 'invoke',
                        },
                    ],
                },
            ],
        }

        const actions = {
            invoke: {
                src: 'perform',
                options: {
                    type: START_INVOKE,
                    payload: {
                        widgetId: 'Page_Table',
                        modelLink: 'test',
                    },
                },
            },
        }

        const defaultMessage = [
            {
                severity: 'info',
                text: 'Информация о виджете',
                details: 'Данные получены',
            },
        ]
        const messageAfterInvoke = [
            {
                severity: 'danger',
                text: 'Это сообщение будет отображаться 10 секунд',
                timeout: 10000,
            },
        ]

        fetchMock
            .restore()
            .get(urlPattern, url => ({
                ...getStubData(url),
                message: defaultMessage,
            }))
            .post(urlPattern, url => ({
                ...getStubData(url),
                message: messageAfterInvoke,
            }))

        return (
            <Factory
                level={WIDGETS}
                {...metadata.Page_Table}
                id="Page_Table"
                actions={actions}
                toolbar={toolbar}
            />
        )
    })
    .add('Ограничение доступа', () => {
        fetchMock.restore().get(urlPattern, url => getStubData(url))

        const security = {
            roles: ['admin'],
        }
        const secureMetadata = set(
            set(
                cloneObject(metadata),
                ['Page_Table', 'table', 'cells', 0, 'security'],
                security,
            ),
            ['Page_Table', 'table', 'headers', 0, 'security'],
            security,
        )

        return (
            <div>
                <small>
          Введите
                    {' '}
                    <mark>admin</mark>
, чтобы увидеть скрытую колонку
                </small>
                <AuthButtonContainer />
                <br />
                <Factory
                    level={WIDGETS}
                    {...secureMetadata.Page_Table}
                    id="Page_Table"
                />
            </div>
        )
    })
    .add('Экшены таблицы', () => {
        fetchMock.restore().get(urlPattern, url => getStubData(url))
        fetchMock.get('begin:n2o/page', page)

        return (
            <Factory
                level={WIDGETS}
                {...tableActions.Page_Table}
                id="Page_Table"
            />
        )
    })
