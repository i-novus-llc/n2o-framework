import { createSlice } from '@reduxjs/toolkit'
import {
    isString,
    map as mapFn,
    pick,
    merge,
    omit,
    set,
    get,
} from 'lodash'

// @ts-ignore ignore import error from js file
import { setIn } from '../../tools/helpers'
import { ModelPrefix } from '../../core/datasource/const'
import { id } from '../../utils/id'

import type { State } from './Models'
import type {
    ClearModelAction, CopyAction, MergeModelAction,
    RemoveAllModelAction, RemoveModelAction, SetModelAction,
    UpdateMapModelAction, UpdateModelAction, FormInitAction,
    AppendFieldToArrayAction, CopyFieldArrayAction, RemoveFieldFromArrayAction,
} from './Actions'

const initialState: State = {
    datasource: {},
    filter: {},
    multi: {}, // selected
    resolve: {},
    edit: {},
}

const modelsSlice = createSlice({
    name: 'n2o/models',
    initialState,
    reducers: {
        SET: {
            prepare(prefix: ModelPrefix, key: string, model: object) {
                return ({
                    payload: { prefix, key, model },
                    meta: { prefix, key, model },
                })
            },

            /**
             * Установка значений модели по префиксу и ключу
             */
            reducer(state, action: SetModelAction) {
                const { key, model, prefix } = action.payload

                set(state, [prefix, key], model)
            },
        },

        REMOVE: {
            prepare(prefix: ModelPrefix, key: string) {
                return ({
                    payload: { prefix, key },
                })
            },

            /**
             * Удаление модели
             */
            reducer(state, action: RemoveModelAction) {
                const { key, prefix } = action.payload

                state[prefix] = omit(state[prefix], key)
            },
        },

        UPDATE: {
            prepare(prefix: ModelPrefix, key: string, field: string, value: unknown) {
                return ({
                    payload: { prefix, key, field, value },
                    meta: { prefix, key, field },
                })
            },

            reducer(state, action: UpdateModelAction) {
                const { prefix, key, field, value } = action.payload

                set(state, `${prefix}.${key}.${field}`, value)
            },
        },

        UPDATE_MAP: {
            prepare(prefix: ModelPrefix, key: string, field: string, value: unknown, map: string) {
                return ({
                    payload: { prefix, key, field, value, map },
                })
            },

            /**
             * Обновление массива с маппингом
             */
            reducer(state, action: UpdateMapModelAction) {
                const { prefix, value, key, field, map } = action.payload
                const newValue = isString(value) ? [value] : value

                state[prefix] = setIn(
                    state[prefix],
                    [key, field],
                    // @ts-ignore FIXME: выяснить что за тип приходит в value
                    mapFn(newValue, v => ({ [map]: v })),
                )
            },
        },

        /**
         * Очистка моделий. которая учивает список исключений (поля которые не нужно очищать)
         */
        CLEAR(state, action: ClearModelAction) {
            const { prefixes, key, exclude } = action.payload

            prefixes.forEach((prefix) => {
                if (state[prefix][key]) {
                    const clearableValue = pick(state[prefix][key], [exclude])

                    set(state, [prefix, key], clearableValue)
                }
            })
        },

        MERGE: {
            prepare(combine: unknown) {
                return ({
                    payload: { combine },
                })
            },

            reducer(state, action: MergeModelAction) {
                const { combine } = action.payload

                return merge(state, combine)
            },
        },

        /**
         * Копирование модели по префиксу и ключу в другую модель, по префиксу и ключу
         */
        COPY: {
            prepare(
                source: CopyAction['payload']['source'],
                target: CopyAction['payload']['target'],
                { mode, sourceMapper }: Pick<CopyAction['payload'], 'mode' | 'sourceMapper'>,
            ) {
                return {
                    payload: {
                        sourceMapper,
                        source,
                        target,
                        mode,
                    },
                }
            },

            reducer() {

            },
        },

        REMOVE_ALL: {
            prepare(key: string) {
                return ({
                    payload: { key },
                })
            },

            /**
             * Удаление всех моделей из хранилища
             */
            reducer(state, action: RemoveAllModelAction) {
                const { key } = action.payload

                Object.values(ModelPrefix).forEach((prefix) => {
                    delete state[prefix][key]
                })
            },
        },

        modelInit: {
            prepare(prefix: ModelPrefix, key: string, model: object, formFirstInit = false) {
                return ({
                    meta: { prefix, key, model, formFirstInit },
                    payload: { prefix, key, model, formFirstInit },
                })
            },

            reducer(state, action: FormInitAction) {
                const { key, model, prefix } = action.payload
                let newState

                if (Object.keys(model).length) {
                    newState = merge(state[prefix][key], model)
                } else {
                    newState = {}
                }

                set(state, [prefix, key], newState)
            },
        },

        appendFieldToArray: {
            prepare({ key, fieldName, ...options }) {
                return ({
                    meta: { key, field: fieldName, ...options },
                    payload: { key, field: fieldName, ...options },
                })
            },

            reducer(state, action: AppendFieldToArrayAction) {
                const { prefix, key, field, value = {}, primaryKey } = action.payload
                const arrayValue = get(state, `${prefix}.${key}.${field}`)
                const item = primaryKey ? { [primaryKey]: id(), ...value } : value

                if (arrayValue) {
                    arrayValue.push(item)
                } else {
                    set(state, `${prefix}.${key}.${field}`, [item])
                }
            },
        },

        removeFieldFromArray: {
            prepare(prefix: ModelPrefix, key: string, field: string, index: number | [number, number]) {
                return ({
                    meta: { prefix, key, field },
                    payload: { prefix, key, field, index },
                })
            },

            reducer(state, action: RemoveFieldFromArrayAction) {
                const { prefix, key, field, index } = action.payload
                const arrayValue = get(state, `${prefix}.${key}.${field}`, [])

                if (typeof index === 'number') {
                    arrayValue.splice(index, 1)

                    return
                }

                arrayValue.splice(...index)
            },
        },

        copyFieldArray: {
            prepare(prefix: ModelPrefix, key: string, field: string, index: number, primaryKey?: string) {
                return ({
                    meta: { prefix, key, field, primaryKey },
                    payload: { prefix, key, field, index, primaryKey },
                })
            },

            reducer(state, action: CopyFieldArrayAction) {
                const { prefix, key, field, index, primaryKey } = action.payload
                const arrayValue = get(state, `${prefix}.${key}.${field}`, [])
                let item = arrayValue[index]

                if (primaryKey) {
                    item = { ...item, [primaryKey]: id() }
                }

                arrayValue.push(item)
            },
        },
    },
})

export default modelsSlice.reducer

export const {
    SET: setModel,
    REMOVE: removeModel,
    UPDATE: updateModel,
    UPDATE_MAP: updateMapModel,
    CLEAR: clearModel,
    MERGE: combineModels,
    COPY: copyModel,
    REMOVE_ALL: removeAllModel,
    modelInit,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
} = modelsSlice.actions
