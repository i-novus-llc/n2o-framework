import React from 'react'
import { storiesOf } from '@storybook/react'
import fetchMock from 'fetch-mock'
import { alertStackedErrors } from 'N2oStorybook/json'

import withPage from '../../../.storybook/decorators/withPage'
import Factory from '../../core/factory/Factory'
import { WIDGETS } from '../../core/factory/factoryLevels'

import widgetWithErrors from './PageWithErrors'

const stories = storiesOf('Функциональность', module)

stories
    .addDecorator(withPage(widgetWithErrors))

    .add('Обработка ошибок', () => {
        const widgetError = {
            meta: {
                alert: {
                    alertKey: 'Page_Form',
                    messages: [
                        {
                            severity: 'danger',
                            text: 'Произошла ошибка при сохранении данных',
                        },
                    ],
                },
            },
        }

        const selectError = {
            meta: {
                alert: {
                    messages: [
                        {
                            severity: 'danger',
                            text: 'Произошла ошибка при взятии данных',
                        },
                    ],
                },
            },
        }

        const invokeError = {
            meta: {
                alert: {
                    alertKey: 'Page_Form',
                    messages: [
                        {
                            severity: 'danger',
                            text: 'Произошла ошибка при сохранении данных',
                        },
                    ],
                },
            },
        }

        fetchMock
            .restore()
            .post('begin:n2o/data', {
                status: 500,
                ...invokeError,
            })
            .get('begin:n2o/data', {
                status: 404,
                body: widgetError,
            })
            .get('begin:n2o/inputSelect', {
                status: 500,
                body: selectError,
            })
        return (
            <Factory
                level={WIDGETS}
                id="Page_Form"
                {...widgetWithErrors.Page_Form}
            />
        )
    })
    .add('Стратегия показа сообщений', () => {
        const widgetError = {
            meta: {
                alert: {
                    alertKey: 'Page_Form',
                    messages: [
                        {
                            severity: 'danger',
                            text: 'Произошла ошибка при сохранении данных',
                        },
                    ],
                },
            },
        }

        const widgetErrorStacked = {
            meta: {
                alert: {
                    alertKey: 'Page_Form',
                    stacked: true,
                    messages: [
                        {
                            severity: 'danger',
                            text: 'Произошла дополнительная ошибка',
                        },
                    ],
                },
            },
        }

        fetchMock
            .restore()
            .post('begin:n2o/data/test', {
                status: 404,
                body: widgetError,
            })
            .post('begin:n2o/data/stacked-test', {
                status: 404,
                body: widgetErrorStacked,
            })
        return (
            <Factory
                level={WIDGETS}
                id="Page_Form"
                {...alertStackedErrors.Page_Form}
            />
        )
    })
