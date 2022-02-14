import { createSlice, createAction } from '@reduxjs/toolkit'
import isArray from 'lodash/isArray'
import isObject from 'lodash/isObject'
import isString from 'lodash/isString'
import mapFn from 'lodash/map'
import pick from 'lodash/pick'
import merge from 'lodash/merge'
import omit from 'lodash/omit'

import { setIn } from '../../tools/helpers'

import { COPY } from './constants'

const initialState = {
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
            /**
             * @param {string} prefix
             * @param {string} key
             * @param {object} model
             * @return {{payload: ModelsStore.setModelPayload}}
             */
            prepare(prefix, key, model) {
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
            reducer(state, action) {
                const { key, model, prefix } = action.payload

                if (!state[prefix]) {
                    state[prefix] = {}
                }

                state[prefix][key] = model
            },
        },

        REMOVE: {
            /**
             * @param {string} prefix
             * @param {string | number} key
             * @return {{payload: ModelsStore.removeModelPayload}}
             */
            prepare(prefix, key) {
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
            reducer(state, action) {
                const { key, prefix } = action.payload

                state[prefix] = omit(state[prefix], key)
            },
        },

        SYNC: {
            /**
             * @param {string} prefix
             * @param {string[]} keys
             * @param {object} model
             * @return {{payload: ModelsStore.syncModelPayload}}
             */
            prepare(prefix, keys, model) {
                return ({
                    payload: { prefix, keys, model },
                })
            },

            /**
             * Установка значений в несколько моделей
             * @param {ModelsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ModelsStore.syncModelPayload} action.payload
             */
            reducer(state, action) {
                const { prefix, keys, model } = action.payload

                state[prefix] = {
                    ...state[prefix],
                    ...keys.map(key => ({ [key]: model })),
                }
            },
        },

        UPDATE: {
            /**
             * @param {string} prefix
             * @param {string} key
             * @param {string} field
             * @param {any} value
             * @return {{payload: ModelsStore.updateModelPayload}}
             */
            prepare(prefix, key, field, value) {
                return ({
                    payload: { prefix, key, field, value },
                })
            },

            /**
             * Обновление значения в модели
             * @param {ModelsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ModelsStore.updateModelPayload} action.payload
             */
            reducer(state, action) {
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
            /**
             * @param {string} prefix
             * @param {string} key
             * @param {string} field
             * @param {any} value
             * @param {string} map
             * @return {{payload: ModelsStore.updateMapModelPayload}}
             */
            prepare(prefix, key, field, value, map) {
                return ({
                    payload: { prefix, key, field, value, map },
                })
            },

            /**
             * Обновление массива с маппингом
             * @param {ModelsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ModelsStore.updateMapModelPayload} action.payload
             */
            reducer(state, action) {
                const { prefix, value, key, field, map } = action.payload
                const newValue = isString(value) ? [value] : value

                state[prefix] = setIn(state[prefix], [key, field], mapFn(newValue, v => ({ [map]: v })))
            },
        },

        /**
         * Очистка моделий. которая учивает список исключений (поля которые не нужно очищать)
         * @param {ModelsStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {ModelsStore.clearModelPayload} action.payload
         */
        CLEAR(state, action) {
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
            reducer(state, action) {
                const { combine } = action.payload

                return merge(state, combine)
            },
        },

        REMOVE_ALL: {
            /**
             * @param {string} key
             * @return {{payload: ModelsStore.removeAllModelPayload}}
             */
            prepare(key) {
                return ({
                    payload: { key },
                })
            },

            /**
             * Удаление всех моделей из хранилища
             * @param {ModelsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ModelsStore.removeAllModelPayload} action.payload
             */
            reducer(state, action) {
                const { key } = action.payload

                Object.keys(state).forEach((prefix) => {
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
