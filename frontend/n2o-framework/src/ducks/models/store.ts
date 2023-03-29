import { createSlice } from '@reduxjs/toolkit'
import {
    merge,
    omit,
    set,
    get,
} from 'lodash'

import { ModelPrefix } from '../../core/datasource/const'
import { id } from '../../utils/id'

import type { State } from './Models'
import type {
    ClearModelAction, CopyAction, MergeModelAction,
    RemoveAllModelAction, RemoveModelAction, SetModelAction,
    UpdateModelAction, AppendFieldToArrayAction, CopyFieldArrayAction,
    RemoveFieldFromArrayAction,
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
            prepare(prefix: ModelPrefix, key: string, model?: object) {
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

        /**
         * Очистка моделий. которая учивает список исключений (поля которые не нужно очищать)
         */
        CLEAR(state, action: ClearModelAction) {
            const { prefixes, key } = action.payload

            prefixes.forEach((prefix) => {
                const model = state[prefix][key]

                if (model) {
                    state[prefix][key] = Array.isArray(model) ? [] : {}
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
            prepare(prefix: ModelPrefix, key: string, field: string, start: number, end?: number) {
                return ({
                    meta: { prefix, key, field },
                    payload: { prefix, key, field, start, end },
                })
            },

            reducer(state, action: RemoveFieldFromArrayAction) {
                const { prefix, key, field, start, end } = action.payload
                const arrayValue = get(state, `${prefix}.${key}.${field}`, [])

                if (end === undefined) {
                    arrayValue.splice(start, 1)

                    return
                }

                arrayValue.splice(start, end)
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
    CLEAR: clearModel,
    MERGE: combineModels,
    COPY: copyModel,
    REMOVE_ALL: removeAllModel,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
} = modelsSlice.actions
