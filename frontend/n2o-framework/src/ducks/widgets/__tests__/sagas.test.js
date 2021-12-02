import { runSaga } from 'redux-saga'
import { put } from 'redux-saga/effects'

import { setModel } from '../../models/store'
import { PREFIXES } from '../../models/constants'

import {
    runResolve,
    clearOnDisable,
    clearForm,
} from '../sagas'
import {
    routesQueryMapping,
} from '../sagas/routesQueryMapping'

describe('Проверка саги widgets', () => {
    describe('тесты routesQueryMapping', () => {
        it('должен вызывать replace', async () => {
            const dispatched = []
            const state = {
                test: {
                    id: 123,
                },
                model: {
                    q: 'qqq',
                },
            }
            const routes = {
                list: [
                    {
                        path: '/test',
                        exact: true,
                        isOtherPage: true,
                    },
                    {
                        path: '/testRoot/:id',
                        exact: true,
                        isOtherPage: true,
                        params: {
                            test: {},
                        },
                    },
                ],
                pathMapping: {
                    id: {
                        link: 'test.id',
                    },
                },
                queryMapping: {
                    q: {
                        set: {
                            value: '`q`',
                            link: 'model',
                        },
                    },
                },
            }
            const location = {
                search: '?name=Sergey',
                pathname: '/testRoot/:id',
                hash: '',
            }
            const fakeStore = {
                getState: () => ({}),
                dispatch: action => dispatched.push(action),
            }
            await runSaga(fakeStore, routesQueryMapping, state, routes, location)
            expect(dispatched[0].type).toBe('@@router/CALL_HISTORY_METHOD')
            expect(dispatched[0].payload.args[0].search).toBe('q=qqq&name=Sergey')
        })
    })
    it('clearForm должен вызвать сброс формы', () => {
        const gen = clearForm({
            payload: {
                key: 'testForm',
            },
        })
        const value = gen.next()

        expect(value.value.type).toBe('PUT')
    })

    it('Должен произойти clearOnDisable', () => {
        const action = {
            payload: {
                widgetId: 'testId',
                modelId: 'testId',
            },
        }
        const gen = clearOnDisable(action)
        expect(gen.next().value.payload).toEqual(
            put(setModel(PREFIXES.datasource, action.payload.widgetId, null)).payload,
        )
    })

    it('Должен произойти runResolve', () => {
        const action = {
            payload: {
                modelId: 'testId',
                model: {
                    some: 'value',
                },
            },
        }
        const gen = runResolve(action)
        expect(gen.next().value.payload).toEqual(
            put(
                setModel(
                    PREFIXES.resolve,
                    action.payload.modelId,
                    action.payload.model,
                ),
            ).payload,
        )
    })
})
