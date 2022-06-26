import { createSlice, createAction } from '@reduxjs/toolkit'
import isArray from 'lodash/isArray'
import isObject from 'lodash/isObject'
import isString from 'lodash/isString'
import mapFn from 'lodash/map'
import pick from 'lodash/pick'
import merge from 'lodash/merge'
import omit from 'lodash/omit'

// @ts-ignore
import { setIn } from '../../tools/helpers'
import { ModelPrefix } from '../../core/datasource/const'


import { ALL_PREFIXES, COPY } from './constants'
import { State } from './Models'
import { ClearModelAction, MergeModelAction, RemoveAllModelAction, RemoveModelAction, SetModelAction, SyncModelAction, UpdateMapModelAction, UpdateModelAction } from './Actions'

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
                })
            },

            /**
             * Установка значений модели по префиксу и ключу
             * @param {ModelsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ModelsStore.setModelPayload} action.payload
             */
            reducer(state, action: SetModelAction) {
                const { key, model, prefix } = action.payload

                if (!state[prefix]) {
                    state[prefix] = {}
                }

                state[prefix][key] = model
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
             * @param {ModelsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ModelsStore.removeModelPayload} action.payload
             */
            reducer(state, action: RemoveModelAction) {
                const { key, prefix } = action.payload

                state[prefix] = omit(state[prefix], key)
            },
        },

        SYNC: {
            prepare(prefix: ModelPrefix, keys: string[], model: object) {
                return ({
                    payload: { prefix, keys, model },
                })
            },

            /**
             * Установка значений в несколько моделей
             */
            reducer(state, action: SyncModelAction) {
                const { prefix, keys, model } = action.payload
                const models = state[prefix]
    
                keys.forEach((key) => {
                    models[key] = model
                })
            },
        },

        UPDATE: {
            prepare(prefix: ModelPrefix, key: string, field: string, value: unknown) {
                return ({
                    payload: { prefix, key, field, value },
                })
            },
            /**
             * Обновление значения в модели
             */
            reducer(state, action: UpdateModelAction) {
                const { prefix, key, field, value } = action.payload

                if (field) {
                    const modelState = state[prefix][key]

                    state[prefix][key] = setIn(
                        isArray(modelState) || isObject(modelState)
                            ? modelState
                            : {}, field, value,
                    )
                }
            },
        },

        UPDATE_MAP: {
            prepare(prefix: ModelPrefix, key: string, field: string, value: unknown, map) {
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
                    // @ts-ignore
                    mapFn(newValue, v => ({ [map]: v }))
                )
            },
        },

        /**
         * Очистка моделий. которая учивает список исключений (поля которые не нужно очищать)
         */
        CLEAR(state, action: ClearModelAction) {
            const { prefixes, key, exclude } = action.payload

            prefixes.forEach((prefix) => {
                if (state[prefix]?.[key]) {
                    state[prefix][key] = pick(state[prefix][key], [exclude])
                }
            })
        },
        MERGE: {
            /**
             * @param {any} combine
             * @return {{payload: ModelsStore.combineModelsPayload}}
             */
            prepare(combine) {
                return ({
                    payload: { combine },
                })
            },

            /**
             * @param {ModelsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ModelsStore.combineModelsPayload} action.payload
             */
            reducer(state, action: MergeModelAction) {
                const { combine } = action.payload

                return merge(state, combine)
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

                ALL_PREFIXES.forEach((prefix) => {
                    delete state[prefix][key]
                })
            },
        },
    },
})

export default modelsSlice.reducer

// Actions
export const {
    SET: setModel,
    REMOVE: removeModel,
    SYNC: syncModel,
    UPDATE: updateModel,
    UPDATE_MAP: updateMapModel,
    CLEAR: clearModel,
    MERGE: combineModels,
    REMOVE_ALL: removeAllModel,
} = modelsSlice.actions

/**
 * Копирование модели по префиксу и ключу в другую модель, по префиксу и ключу
 * @param {Object} source
 * @param {string} source.prefix
 * @param {string} source.key
 * @param {Object} target
 * @param {string} target.prefix
 * @param {string} target.key
 * @param {Object} settings
 * @param {string} settings.mode
 * @param {any} settings.sourceMapper
 */
export const copyModel = createAction(COPY, (source, target, { mode, sourceMapper }) => ({
    payload: {
        sourceMapper,
        source,
        target,
        mode,
    },
}))
