import { createSlice } from '@reduxjs/toolkit'
import merge from 'lodash/merge'
import omit from 'lodash/omit'
import set from 'lodash/set'
import get from 'lodash/get'
import isEqual from 'lodash/isEqual'

import { ModelPrefix, ModelLink, Model } from '../../core/models/types'
import { getFieldPath, getModelPath } from '../../core/models/getModelPath'
import { id } from '../../utils/id'
import { setKey } from '../../utils/uniqueKey'
import { N2OMeta } from '../Action'

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
                modelLink: ModelLink<Prefix>,
                model: (
                    Prefix extends (ModelPrefix.source | ModelPrefix.selected)
                        ? Model[]
                        : Model
                ) | null,
                isDefault?: boolean,
                validate?: boolean,
            ) {
                return ({
                    payload: { modelLink, model, isDefault },
                    meta: { validate: typeof validate === 'boolean' ? validate : !isDefault } as N2OMeta,
                })
            },

            /**
             * Установка значений модели по префиксу и ключу
             */
            reducer(state, action: SetModelAction) {
                const { model, modelLink } = action.payload
                const path = getModelPath(modelLink)

                if (isEqual(get(state, path), model)) { return }

                set(state, path, setKey(model))
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

        updateModel: {
            prepare(modelLink: ModelLink, fieldName: string, value: unknown, validate?: boolean) {
                return ({
                    payload: { modelLink, fieldName, value },
                    meta: { validate: typeof validate === 'boolean' ? validate : true } as N2OMeta,
                })
            },

            reducer(state, action: UpdateModelAction) {
                const { modelLink, fieldName, value } = action.payload

                set(state, getFieldPath({ ...modelLink, field: fieldName }), setKey(value))
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
            prepare({ modelLink, fieldName, position, ...options }) {
                return ({
                    meta: { ...options },
                    payload: { modelLink, fieldName, position, ...options },
                })
            },

            reducer(state, action: AppendToArrayAction) {
                const { modelLink, fieldName, value = {}, primaryKey, position } = action.payload
                const path = fieldName ? getFieldPath({ ...modelLink, field: fieldName }) : getModelPath(modelLink)
                const arrayValue = get(state, path)
                const item = setKey(primaryKey ? { [primaryKey]: id(), ...value } : { ...value })

                if (arrayValue) {
                    arrayValue.splice(position ?? arrayValue.length, 0, item)
                } else {
                    set(state, path, [item])
                }
            },
        },

        removeFromArray: {
            prepare({ modelLink, fieldName, start, count = 1 }) {
                return ({
                    payload: { modelLink, fieldName, start, count },
                })
            },

            reducer(state, action: RemoveFromArrayAction) {
                const { modelLink, fieldName, start, count } = action.payload
                const path = fieldName ? getFieldPath({ ...modelLink, field: fieldName }) : getModelPath(modelLink)
                const arrayValue: unknown[] = get(state, path, [])

                arrayValue.splice(start, count ?? 1)
            },
        },

        copyFieldArray: {
            prepare(modelLink: ModelLink, fieldName: string, index: number, primaryKey?: string) {
                return ({
                    payload: { modelLink, fieldName, index, primaryKey },
                })
            },

            reducer(state, action: CopyFieldArrayAction) {
                const { modelLink, fieldName, index, primaryKey } = action.payload
                const arrayValue = get(state, getFieldPath({ ...modelLink, field: fieldName }), [])
                const item = setKey({ ...arrayValue[index] })

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
    updateModel,
    CLEAR: clearModel,
    MERGE: combineModels,
    COPY: copyModel,
    REMOVE_ALL: removeAllModel,
    appendToArray,
    removeFromArray,
    copyFieldArray,
} = modelsSlice.actions
