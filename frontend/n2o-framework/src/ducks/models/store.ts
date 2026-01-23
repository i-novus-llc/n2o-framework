import { createSlice } from '@reduxjs/toolkit'
import merge from 'lodash/merge'
import omit from 'lodash/omit'
import set from 'lodash/set'
import get from 'lodash/get'

import { ModelPrefix } from '../../core/datasource/const'
import { id } from '../../utils/id'

import type { State } from './Models'
import type {
    ClearModelAction, CopyAction, MergeModelAction,
    RemoveAllModelAction, RemoveModelAction, SetModelAction,
    UpdateModelAction, AppendToArrayAction, CopyFieldArrayAction,
    RemoveFromArrayAction,
} from './Actions'

export const initialState: State = {
    datasource: {},
    filter: {},
    multi: {}, // selected
    resolve: {},
    edit: {},
}

export const modelsSlice = createSlice({
    name: 'n2o/models',
    initialState,
    reducers: {
        SET: {
            prepare<Prefix extends ModelPrefix>(
                prefix: Prefix,
                key: string,
                model: (Prefix extends (ModelPrefix.source | ModelPrefix.selected)
                    ? Array<Record<string, unknown>>
                    : Record<string, unknown>) | null,
                isDefault?: boolean,
                validate?: boolean,
            ) {
                return ({
                    payload: { prefix, key, model, isDefault },
                    meta: { prefix, key, model, validate: typeof validate === 'boolean' ? validate : !isDefault },
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
            prepare(prefix: ModelPrefix, key: string, field: string, value: unknown, validate?: boolean) {
                return ({
                    payload: { prefix, key, field, value },
                    meta: { validate: typeof validate === 'boolean' ? validate : true },
                })
            },

            reducer(state, action: UpdateModelAction) {
                const { prefix, key, field, value } = action.payload

                set(state, `${prefix}.${key}.${field}`, value)
            },
        },

        /**
         * Очистка моделей. которая учитывает список исключений (поля которые не нужно очищать)
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
            prepare(combine: Partial<State>) {
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
            // eslint-disable-next-line sonarjs/no-identical-functions
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

        appendToArray: {
            prepare({ key, field, position, ...options }) {
                return ({
                    meta: { key, field, ...options },
                    payload: { key, field, position, ...options },
                })
            },

            reducer(state, action: AppendToArrayAction) {
                const { prefix, key, field, value = {}, primaryKey, position } = action.payload
                const path = field ? `${prefix}.${key}.${field}` : `${prefix}.${key}`
                const arrayValue = get(state, path)
                const item = primaryKey ? { [primaryKey]: id(), ...value } : { ...value }

                if (arrayValue) {
                    arrayValue.splice(position ?? arrayValue.length, 0, item)
                } else {
                    set(state, path, [item])
                }
            },
        },

        removeFromArray: {
            prepare({ prefix, key, field, start, count }) {
                return ({
                    meta: { prefix, key, field },
                    payload: { prefix, key, field, start, count },
                })
            },

            reducer(state, action: RemoveFromArrayAction) {
                const { prefix, key, field, start, count } = action.payload
                const path = field ? `${prefix}.${key}.${field}` : `${prefix}.${key}`
                const arrayValue: unknown[] = get(state, path, [])

                arrayValue.splice(start, count ?? 1)
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
                const item = { ...arrayValue[index] }

                if (primaryKey) { item[primaryKey] = id() }

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
    appendToArray,
    removeFromArray,
    copyFieldArray,
} = modelsSlice.actions
