import { runSaga } from 'redux-saga'

import {
    routesQueryMapping,
} from '../sagas/routesQueryMapping'

describe('Проверка саги datasource', () => {
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
})
