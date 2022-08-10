import { runSaga } from 'redux-saga'
import { put } from 'redux-saga/effects'
import { touch } from 'redux-form'

import { addTouched, copyAction, clearForm } from '../sagas'

const state = {
    models: {
        resolve: {
            someWidget: {
                notFieldMerge: 'test',
                name: 'Лада',
                merge: {
                    name: 'name',
                    surname: 'surname',
                },
                add: ['first', 'second'],
                replace: {
                    some: 'value',
                },
            },
            anotherWidget: {
                anotherWidgettest: 'test',
                name: 'Lada',
                merge: {
                    partname: 'partname',
                },
                add: ['third'],
                replace: {
                    another: 'value',
                },
            },
        },
    },
}

describe('Проверка саги formPlugin', () => {
    it('Проверка вызова touch при добавлении сообщения', () => {
        const testData = {
            payload: {
                name: 'formField',
                form: 'formName',
            },
        }

        const genAddMessage = addTouched(testData)

        expect(genAddMessage.next().value).toEqual(
            put(touch(testData.payload.form, testData.payload.name)),
        )
        expect(genAddMessage.next().done).toEqual(true)
    })

    it.skip('clearForm должен вызвать сброс формы', () => {
        const gen = clearForm({
            payload: {
                key: 'testForm',
            },
        })
        const delay = gen.next()
        const clear = gen.next()

        expect(clear.value.type).toBe('PUT')
    })

    describe('копирование mode = merge', () => {
        it('должен выполнить копирование mode = merge, field field', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({ ...state }),
                dispatch: action => dispatched.push(action),
            }

            await runSaga(fakeStore, copyAction, {
                payload: {
                    target: {
                        prefix: 'resolve',
                        key: 'anotherWidget',
                        field: 'merge',
                    },
                    source: {
                        prefix: 'resolve',
                        key: 'someWidget',
                        field: 'merge',
                    },
                    mode: 'merge',
                },
            })

            expect(dispatched[0].payload).toEqual({
                prefix: 'resolve',
                key: 'anotherWidget',
                model: {
                    name: 'Lada',
                    replace: {
                        another: 'value',
                    },
                    add: ['third'],
                    anotherWidgettest: 'test',
                    merge: {
                        name: 'name',
                        surname: 'surname',
                        partname: 'partname',
                    },
                },
            })
        })
        it('должен выполнить копирование mode = merge, !field !field', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({ ...state }),
                dispatch: action => dispatched.push(action),
            }

            await runSaga(fakeStore, copyAction, {
                payload: {
                    target: {
                        prefix: 'resolve',
                        key: 'anotherWidget',
                    },
                    source: {
                        prefix: 'resolve',
                        key: 'someWidget',
                    },
                    mode: 'merge',
                },
            })

            expect(dispatched[0].payload).toEqual({
                prefix: 'resolve',
                key: 'anotherWidget',
                model: {
                    notFieldMerge: 'test',
                    anotherWidgettest: 'test',
                    name: 'Лада',
                    merge: {
                        name: 'name',
                        surname: 'surname',
                    },
                    add: ['first', 'second'],
                    replace: {
                        some: 'value',
                    },
                },
            })
        })
        it('должен выполнить копирование mode = merge, field !field', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({ ...state }),
                dispatch: action => dispatched.push(action),
            }

            await runSaga(fakeStore, copyAction, {
                payload: {
                    target: {
                        prefix: 'resolve',
                        key: 'anotherWidget',
                        field: 'merge',
                    },
                    source: {
                        prefix: 'resolve',
                        key: 'someWidget',
                    },
                    mode: 'merge',
                },
            })

            expect(dispatched[0].payload).toEqual({
                prefix: 'resolve',
                key: 'anotherWidget',
                model: {
                    anotherWidgettest: 'test',
                    name: 'Lada',
                    merge: {
                        partname: 'partname',
                        notFieldMerge: 'test',
                        name: 'Лада',
                        merge: {
                            name: 'name',
                            surname: 'surname',
                        },
                        add: ['first', 'second'],
                        replace: {
                            some: 'value',
                        },
                    },
                    add: ['third'],
                    replace: {
                        another: 'value',
                    },
                },
            })
        })
        it('должен выполнить копирование mode = merge, !field field', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({ ...state }),
                dispatch: action => dispatched.push(action),
            }

            await runSaga(fakeStore, copyAction, {
                payload: {
                    target: {
                        prefix: 'resolve',
                        key: 'anotherWidget',
                    },
                    source: {
                        prefix: 'resolve',
                        key: 'someWidget',
                        field: 'merge',
                    },
                    mode: 'merge',
                },
            })

            expect(dispatched[0].payload).toEqual({
                prefix: 'resolve',
                key: 'anotherWidget',
                model: {
                    anotherWidgettest: 'test',
                    name: 'name',
                    surname: 'surname',
                    merge: {
                        partname: 'partname',
                    },
                    add: ['third'],
                    replace: {
                        another: 'value',
                    },
                },
            })
        })
    })

    describe('копирование mode = add', () => {
        it('должен выполнить копирование mode = add, field field', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({ ...state }),
                dispatch: action => dispatched.push(action),
            }

            await runSaga(fakeStore, copyAction, {
                payload: {
                    target: {
                        prefix: 'resolve',
                        key: 'anotherWidget',
                        field: 'add',
                    },
                    source: {
                        prefix: 'resolve',
                        key: 'someWidget',
                        field: 'add',
                    },
                    mode: 'add',
                },
            })

            expect(dispatched[0].payload).toEqual({
                prefix: 'resolve',
                key: 'anotherWidget',
                model: {
                    anotherWidgettest: 'test',
                    replace: {
                        another: 'value',
                    },
                    name: 'Lada',
                    merge: {
                        partname: 'partname',
                    },
                    add: ['third', 'first', 'second'],
                },
            })
        })
    })

    describe('копирование mode = replace', () => {
        it('должен выполнить копирование mode = replace, field field', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({ ...state }),
                dispatch: action => dispatched.push(action),
            }

            await runSaga(fakeStore, copyAction, {
                payload: {
                    target: {
                        prefix: 'resolve',
                        key: 'anotherWidget',
                        field: 'replace',
                    },
                    source: {
                        prefix: 'resolve',
                        key: 'someWidget',
                        field: 'replace',
                    },
                    mode: 'replace',
                },
            })

            expect(dispatched[0].payload).toEqual({
                prefix: 'resolve',
                key: 'anotherWidget',
                model: {
                    add: ['third'],
                    merge: {
                        partname: 'partname',
                    },
                    anotherWidgettest: 'test',
                    name: 'Lada',
                    replace: {
                        some: 'value',
                    },
                },
            })
        })
        it('должен выполнить копирование mode = replace, !field !field', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({ ...state }),
                dispatch: action => dispatched.push(action),
            }

            await runSaga(fakeStore, copyAction, {
                payload: {
                    target: {
                        prefix: 'resolve',
                        key: 'anotherWidget',
                    },
                    source: {
                        prefix: 'resolve',
                        key: 'someWidget',
                    },
                    mode: 'replace',
                },
            })

            expect(dispatched[0].payload).toEqual({
                prefix: 'resolve',
                key: 'anotherWidget',
                model: {
                    name: 'Лада',
                    merge: {
                        name: 'name',
                        surname: 'surname',
                    },
                    add: ['first', 'second'],
                    notFieldMerge: 'test',
                    replace: {
                        some: 'value',
                    },
                },
            })
        })
        it('должен выполнить копирование mode = replace, field !field', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({ ...state }),
                dispatch: action => dispatched.push(action),
            }

            await runSaga(fakeStore, copyAction, {
                payload: {
                    target: {
                        prefix: 'resolve',
                        key: 'anotherWidget',
                        field: 'replace',
                    },
                    source: {
                        prefix: 'resolve',
                        key: 'someWidget',
                    },
                    mode: 'replace',
                },
            })

            expect(dispatched[0].payload).toEqual({
                prefix: 'resolve',
                key: 'anotherWidget',
                model: {
                    name: 'Lada',
                    merge: {
                        partname: 'partname',
                    },
                    anotherWidgettest: 'test',
                    add: ['third'],
                    replace: {
                        name: 'Лада',
                        merge: {
                            name: 'name',
                            surname: 'surname',
                        },
                        add: ['first', 'second'],
                        notFieldMerge: 'test',
                        replace: {
                            some: 'value',
                        },
                    },
                },
            })
        })
        it('должен выполнить копирование mode = replace, !field field', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({ ...state }),
                dispatch: action => dispatched.push(action),
            }

            await runSaga(fakeStore, copyAction, {
                payload: {
                    target: {
                        prefix: 'resolve',
                        key: 'anotherWidget',
                    },
                    source: {
                        prefix: 'resolve',
                        key: 'someWidget',
                        field: 'replace',
                    },
                    mode: 'replace',
                },
            })

            expect(dispatched[0].payload).toEqual({
                prefix: 'resolve',
                key: 'anotherWidget',
                model: {
                    some: 'value',
                },
            })
        })
    })
})
