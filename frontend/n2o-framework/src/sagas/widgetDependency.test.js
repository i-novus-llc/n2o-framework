import { runSaga } from 'redux-saga'

import { DEPENDENCY_TYPES } from '../core/dependencyTypes'
import {
    DATA_REQUEST,
    DISABLE,
    ENABLE,
    HIDE,
    SHOW,
} from '../constants/widgets'

import {
    reduceFunction,
    registerWidgetDependency,
    resolveDependency,
    sortDependency,
    resolveWidgetDependency,
    forceUpdateDependency,
} from './widgetDependency'

const getConfig = (model, config) => ({
    model,
    config,
})

const widgetsDependencies = registerWidgetDependency({}, 'test1', {
    fetch: [
        {
            on: 'models.resolve[\'testWidget\']',
        },
    ],
})

const prevState = {
    widgets: {
        test1: {
            isVisible: true,
        },
    },
    models: {
        resolve: {
            testWidget: {
                name: 'Ivan',
            },
        },
    },
}
const state = {
    widgets: {
        testWidget: {
            isVisible: true,
        },
    },
    models: {
        resolve: {
            testWidget: {
                name: 'Sergey',
            },
        },
    },
}

describe('Проверка саги widgetDependency', () => {
    describe('тесты forceUpdateDependency', () => {
        it('должен вызвать разрешение зависимости', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({
                    widgets: {
                        test1: {
                            isVisible: true,
                        },
                    },
                    models: {
                        resolve: {
                            testWidget: {
                                name: 'Sergey',
                            },
                        },
                    },
                }),
                dispatch: action => dispatched.push(action),
            }
            await runSaga(
                fakeStore,
                forceUpdateDependency,
                state,
                widgetsDependencies,
                'testWidget',
            )

            expect(dispatched[0].type).toEqual(DATA_REQUEST)
        })
    })
    describe('тесты resolveWidgetDependency', () => {
        it('должен вызвать разрешение зависимости', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({
                    widgets: {
                        test1: {
                            isVisible: true,
                        },
                    },
                    models: {
                        resolve: {
                            testWidget: {
                                name: 'Sergey',
                            },
                        },
                    },
                }),
                dispatch: action => dispatched.push(action),
            }
            await runSaga(
                fakeStore,
                resolveWidgetDependency,
                prevState,
                state,
                widgetsDependencies,
            )

            expect(dispatched[0].type).toBe(DATA_REQUEST)
        })
    })
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

    describe('registerWidgetDependency', () => {
        it('вернет новое значение виджетов', () => {
            expect(
                registerWidgetDependency({}, 'test1', {
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
        it('вызовет resolveFetchDependency', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({}),
                dispatch: action => dispatched.push(action),
            }
            await runSaga(
                fakeStore,
                resolveDependency,
                DEPENDENCY_TYPES.fetch,
                'test1',
                {},
                true,
            )
            expect(dispatched[0].type).toEqual(DATA_REQUEST)
            expect(dispatched[0].payload).toEqual({
                widgetId: 'test1',
                options: {},
            })
        })

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
                expect(dispatched[0].type).toEqual(SHOW)
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
                expect(dispatched[0].type).toEqual(HIDE)
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
                expect(dispatched[0].type).toEqual(ENABLE)
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
                expect(dispatched[0].type).toEqual(DISABLE)
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
