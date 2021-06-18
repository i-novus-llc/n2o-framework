import { createSlice } from '@reduxjs/toolkit'

import { RESET_STATE } from '../../constants/widgets'

import ColumnsResolver from './ColumnsResolver'

const columnsSlice = createSlice({
    name: 'n2o/columns',
    initialState: {},
    reducers: {
        REGISTER_COLUMN: {
            /**
             * @param {string} widgetId
             * @param {string} columnId
             * @param {string} label
             * @param {boolean} disabled
             * @param {boolean} visible
             * @param {object} conditions
             * @returns {{payload: ColumnsStore.registerColumnPayload}}
             */
            prepare(
                widgetId,
                columnId,
                label,
                visible,
                disabled,
                conditions,
            ) {
                return ({
                    payload: {
                        key: widgetId,
                        columnId,
                        label,
                        visible,
                        disabled,
                        conditions,
                    },
                })
            },

            /**
             * Зарегистрировать колонку в редаксе
             * @param {ColumnsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ColumnsStore.registerColumnPayload} action.payload
             */
            reducer(state, action) {
                const { key, columnId } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                state[key][columnId] = { ...ColumnsResolver.defaultState, ...action.payload }
            },
        },

        CHANGE_COLUMN_VISIBILITY: {
            /**
             * @param {string} widgetId
             * @param {string} columnId
             * @param {boolean} visible
             * @returns {{payload: ColumnsStore.changeVisibilityPayload}}
             */
            prepare(widgetId, columnId, visible) {
                return ({
                    payload: {
                        visible,
                        key: widgetId,
                        columnId,
                    },
                })
            },

            /**
             * Изменение видимсоти колонки таблицы
             * @param {ColumnsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ColumnsStore.changeVisibilityPayload} action.payload
             */
            reducer(state, action) {
                const { key, columnId, visible } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                if (!state[key][columnId]) {
                    state[key][columnId] = ColumnsResolver.defaultState
                }

                state[key][columnId].visible = visible
            },
        },

        CHANGE_COLUMN_DISABLED: {
            /**
             * @param {string} widgetId
             * @param {string} columnId
             * @param {boolean} disabled
             * @returns {{payload: ColumnsStore.changeDisabledPayload}}
             */
            prepare(widgetId, columnId, disabled) {
                return ({
                    payload: {
                        disabled,
                        key: widgetId,
                        columnId,
                    },
                })
            },

            /**
             * Изменение активности колонки таблицы
             * @param {ColumnsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ColumnsStore.changeDisabledPayload} action.payload
             */
            reducer(state, action) {
                const { key, columnId, disabled } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                if (!state[key][columnId]) {
                    state[key][columnId] = ColumnsResolver.defaultState
                }

                state[key][columnId].disabled = disabled
            },
        },

        TOGGLE_COLUMN_VISIBILITY: {
            /**
             * @param {string} widgetId
             * @param {string} columnId
             * @returns {{payload: ColumnsStore.toggleVisibilityPayload}}
             */
            prepare(widgetId, columnId) {
                return ({
                    payload: {
                        key: widgetId,
                        columnId,
                    },
                })
            },

            /**
             * Переключение видимости колонки таблицы
             * @param {ColumnsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ColumnsStore.toggleVisibilityPayload} action.payload
             */
            reducer(state, action) {
                const { key, columnId } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                if (!state[key][columnId]) {
                    state[key][columnId] = ColumnsResolver.defaultState
                }

                state[key][columnId].visible = !state[key][columnId].visible
            },
        },

        CHANGE_FROZEN_COLUMN: {
            /**
             * @param {string} widgetId
             * @param {string} columnId
             * @returns {{payload: ColumnsStore.changeFrozenPayload}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId, columnId) {
                return ({
                    payload: {
                        key: widgetId,
                        columnId,
                    },
                })
            },

            /**
             * Переключение флага frozen
             * @param {ColumnsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ColumnsStore.changeFrozenPayload} action.payload
             */
            reducer(state, action) {
                const { key, columnId } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                if (!state[key][columnId]) {
                    state[key][columnId] = ColumnsResolver.defaultState
                }

                state[key][columnId].frozen = !state[key][columnId].frozen
            },
        },
    },
    extraReducers: {
        /**
         * Переключение флага frozen
         * @param {ColumnsStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {{ widgetId: string }} action.payload
         */
        [RESET_STATE](state, action) {
            const { widgetId } = action.payload

            if (state[widgetId]) {
                Object.keys(state[widgetId]).forEach((key) => {
                    state[widgetId][key].isInit = false
                })
            }
        },
    },
})

export default columnsSlice.reducer

// Actions
export const {
    CHANGE_COLUMN_DISABLED: changeColumnDisabled,
    CHANGE_COLUMN_VISIBILITY: changeColumnVisibility,
    CHANGE_FROZEN_COLUMN: changeFrozenColumn,
    REGISTER_COLUMN: registerColumn,
    TOGGLE_COLUMN_VISIBILITY: toggleColumnVisibility,
} = columnsSlice.actions

export const setColumnVisible = (widgetId, columnId) => changeColumnVisibility(widgetId, columnId, true)

export const setColumnHidden = (widgetId, columnId) => changeColumnVisibility(widgetId, columnId, false)
