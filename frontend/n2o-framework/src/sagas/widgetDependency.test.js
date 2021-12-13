import { runSaga } from 'redux-saga'

import { DEPENDENCY_TYPES } from '../core/dependencyTypes'

import {
    disableWidget,
    enableWidget,
    hideWidget,
    showWidget
} from '../ducks/widgets/store'

import { sortDependency } from './widgetDependency/sortDependency'
import { reduceFunction, resolveDependency } from './widgetDependency/resolve'
import { getWidgetDependency } from './widgetDependency/getWidgetDependency'

const getConfig = (model, config) => ({
    model,
    config,
})

describe('Проверка саги widgetDependency', () => {
    describe('reduceFunction', () => {
        it('вернет false', () => {
            const model = {
                test: 'test',
            }
            const config = {
                condition: 'test !== "test"',
            }

            expect(reduceFunction(false, getConfig(model, config))).toEqual(false)
            expect(reduceFunction(true, getConfig(model, config))).toEqual(false)
        })
        it('вернет true', () => {
            const model = {
                test: 'test',
            }
            const config = {
                condition: 'test == "test"',
            }

            expect(reduceFunction(true, getConfig(model, config))).toEqual(true)
        })
    })

    describe('getWidgetDependency', () => {
        it('вернет новое значение виджетов', () => {
            expect(
                getWidgetDependency({}, 'test1', {
                    fetch: [
                        {
                            on: 'link',
                            condition: 'condition',
                        },
                    ],
                }),
            ).toEqual({
                test1: {
                    widgetId: 'test1',
                    dependency: {
                        fetch: [
                            {
                                on: 'link',
                                condition: 'condition',
                            },
                        ],
                    },
                    parents: ['link'],
                },
            })
        })
    })

    describe('resolveDependency', () => {

        describe('resolveVisibleDependency', () => {
            it('покажет виджет', async () => {
                const dispatched = []
                const fakeStore = {
                    getState: () => ({
                        models: {
                            resolve: {
                                test1: {
                                    id: 1,
                                },
                            },
                        },
                    }),
                    dispatch: action => dispatched.push(action),
                }
                await runSaga(
                    fakeStore,
                    resolveDependency,
                    DEPENDENCY_TYPES.visible,
                    'test1',
                    [
                        {
                            model: { id: 2 },
                            config: {
                                on: 'models.resolve.test1',
                                condition: 'id !== 1',
                            },
                        },
                    ],
                )
                expect(dispatched[0].type).toEqual(showWidget.type)
                expect(dispatched[0].payload).toEqual({
                    widgetId: 'test1',
                })
            })

            it('скроет виджет', async () => {
                const dispatched = []
                const fakeStore = {
                    getState: () => ({
                        models: {
                            resolve: {
                                test1: {
                                    id: 1,
                                },
                            },
                        },
                    }),
                    dispatch: action => dispatched.push(action),
                }
                await runSaga(
                    fakeStore,
                    resolveDependency,
                    DEPENDENCY_TYPES.visible,
                    'test1',
                    [
                        {
                            model: { id: 2 },
                            config: {
                                on: 'models.resolve.test1',
                                condition: 'id === 1',
                            },
                        },
                    ],
                )

                expect(dispatched[0].type).toEqual(hideWidget.type)
                expect(dispatched[0].payload).toEqual({
                    widgetId: 'test1',
                })
            })
        })

        describe('resolveEnabledDependency', () => {
            it('активирует виджет', async () => {
                const dispatched = []
                const fakeStore = {
                    getState: () => ({}),
                    dispatch: action => dispatched.push(action),
                }
                await runSaga(
                    fakeStore,
                    resolveDependency,
                    DEPENDENCY_TYPES.enabled,
                    'test1',
                    [
                        {
                            model: { id: 2 },
                            config: {
                                on: 'models.resolve.test1',
                                condition: 'id !== 1',
                            },
                        },
                    ],
                )
                expect(dispatched[0].type).toEqual(enableWidget.type)
                expect(dispatched[0].payload).toEqual({
                    widgetId: 'test1',
                })
            })

            it('дизактивирует виджет', async () => {
                const dispatched = []
                const fakeStore = {
                    getState: () => ({}),
                    dispatch: action => dispatched.push(action),
                }
                await runSaga(
                    fakeStore,
                    resolveDependency,
                    DEPENDENCY_TYPES.enabled,
                    'test1',
                    [
                        {
                            model: { id: 2 },
                            config: {
                                on: 'models.resolve.test1',
                                condition: 'id === 1',
                            },
                        },
                    ],
                )
                expect(dispatched[0].type).toEqual(disableWidget.type)
                expect(dispatched[0].payload).toEqual({
                    widgetId: 'test1',
                })
            })
        })
    })
    describe('sortDependency', () => {
        it('проверка правильности сортировки', () => {
            expect(
                sortDependency({
                    fetch: [],
                    visible: [],
                    enabled: [],
                }),
            ).toEqual({ visible: [], enabled: [], fetch: [] })
            expect(
                sortDependency({
                    enabled: [],
                    fetch: [],
                    visible: [],
                }),
            ).toEqual({ enabled: [], visible: [], fetch: [] })
            expect(
                sortDependency({
                    enabled: [],
                    visible: [],
                    fetch: [],
                }),
            ).toEqual({ enabled: [], visible: [], fetch: [] })
        })
    })
})
