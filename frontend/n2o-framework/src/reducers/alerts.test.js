import { ADD, ADD_MULTI, REMOVE, REMOVE_ALL } from '../constants/alerts'

import alerts from './alerts'

describe('Тесты alerts reducer', () => {
    it('Проверка ADD', () => {
        const reducer = alerts(
            {},
            {
                type: ADD,
                payload: {
                    key: 'alerts',
                    id: 2,
                    label: 'second label',
                    text: 'second text',
                },
            },
        )

        expect(reducer).toEqual({
            alerts: [
                {
                    closeButton: undefined,
                    details: undefined,
                    id: 2,
                    label: 'second label',
                    severity: undefined,
                    text: 'second text',
                },
            ],
        })
    })

    it('Проверка ADD_MULTI', () => {
        const reducer = alerts(
            {
                alerts: [],
            },
            {
                type: ADD_MULTI,
                payload: {
                    key: 'alerts',
                    alerts: [
                        {
                            id: 2,
                            label: 'second label',
                            text: 'second text',
                        },
                        {
                            id: 3,
                            label: 'third label',
                            text: 'third text',
                        },
                    ],
                },
            },
        )

        expect(reducer.alerts[0].label).toEqual('second label')
        expect(reducer.alerts[0].text).toEqual('second text')

        expect(reducer.alerts[1].label).toEqual('third label')
        expect(reducer.alerts[1].text).toEqual('third text')
    })

    it('Проверка REMOVE', () => {
        const reducer = alerts(
            {
                alerts: [
                    {
                        id: 2,
                        label: 'second label',
                        text: 'second text',
                    },
                    {
                        id: 3,
                        label: 'third label',
                        text: 'third text',
                    },
                ],
            },
            {
                type: REMOVE,
                payload: {
                    key: 'alerts',
                    id: 2,
                },
            },
        )

        expect(reducer).toEqual({
            alerts: [
                {
                    id: 3,
                    label: 'third label',
                    text: 'third text',
                },
            ],
        })
    })

    it('Проверка REMOVE_ALL', () => {
        const reducer = alerts(
            {
                alerts: [
                    {
                        id: 2,
                        label: 'second label',
                        text: 'second text',
                    },
                    {
                        id: 3,
                        label: 'third label',
                        text: 'third text',
                    },
                ],
            },
            {
                type: REMOVE_ALL,
                payload: {
                    key: 'alerts',
                    id: 2,
                },
            },
        )

        expect(reducer).toEqual({})
    })
})
