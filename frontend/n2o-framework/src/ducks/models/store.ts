import { createSlice, createAction } from '@reduxjs/toolkit'
import {
    isArray,
    isObject,
    isString,
    map as mapFn,
    merge,
    omit,
} from 'lodash'

// @ts-ignore ignore import error from js file
import { setIn } from '../../tools/helpers'
import type { ModelPrefix } from '../../core/datasource/const'

import { ALL_PREFIXES, COPY } from './constants'
import type { State } from './Models'
import type {
    ClearModelAction, CopyAction, MergeModelAction,
    RemoveAllModelAction, RemoveModelAction, SetModelAction,
    UpdateMapModelAction, UpdateModelAction,
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
        // eslint-disable-next-line @typescript-eslint/naming-convention
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

                state[prefix][key] = model
            },
        },

        // eslint-disable-next-line @typescript-eslint/naming-convention
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

        // eslint-disable-next-line @typescript-eslint/naming-convention
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

        // eslint-disable-next-line @typescript-eslint/naming-convention
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
        // eslint-disable-next-line @typescript-eslint/naming-convention
        CLEAR(state, action: ClearModelAction) {
            const { prefixes, key } = action.payload

            prefixes.forEach((prefix) => {
                const model = state[prefix][key]

                if (model) {
                    state[prefix][key] = Array.isArray(model) ? [] : {}
                }
            })
        },

        // eslint-disable-next-line @typescript-eslint/naming-convention
        MERGE: {
            /**
             * @param {any} combine
             * @return {{payload: ModelsStore.combineModelsPayload}}
             */
            prepare(combine: unknown) {
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

        // eslint-disable-next-line @typescript-eslint/naming-convention
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
export const copyModel = createAction(
    COPY,
    (
        source: CopyAction['payload']['source'],
        target: CopyAction['payload']['target'],
        { mode, sourceMapper }: Pick<CopyAction['payload'], 'mode' | 'sourceMapper'>,
    ) => ({
        payload: {
            sourceMapper,
            source,
            target,
            mode,
        },
    }),
)
