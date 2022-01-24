import { createSlice, createAction } from '@reduxjs/toolkit'

import WidgetResolver from './WidgetResolver'
import { ALERT_ADD, ALERT_REMOVE, SET_WIDGET_METADATA } from './constants'

const initialState = {}

const widgetSlice = createSlice({
    name: 'n2o/widgets',
    initialState,
    reducers: {
        REGISTER: {
            /**
             * @param {string} widgetId
             * @param {object} initProps
             * @param {boolean} [preInit]
             * @return {{payload: WidgetsStore.registerWidgetPayload}}
             */
            prepare(widgetId, initProps, preInit) {
                return ({
                    payload: { widgetId, initProps, preInit },
                })
            },
            /**
             * Регистрация виджета
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {Columns.registerWidgetPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId, initProps, preInit } = action.payload
                const currentState = state[widgetId] || {}

                state[widgetId] = {
                    ...WidgetResolver.defaultState,
                    ...currentState,
                    ...initProps,
                    isInit: !preInit,
                    type: initProps.type,
                }
            },
        },

        RESOLVE: {
            /**
             * @param {string} widgetId
             * @param {object} resolveModel
             * @param {string} modelId
             * @return {{payload: WidgetsStore.resolveWidgetPayload}}
             */
            prepare(widgetId, resolveModel, modelId) {
                return ({
                    payload: { widgetId, model: resolveModel, modelId },
                })
            },

            /**
             * Вызывает действие разрешения виджета.
             * Side-effect: зависимости, простановка в resolve-модель
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {Columns.resolveWidgetPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isResolved = true
            },
        },

        SHOW: {
            /**
             * @param {string} widgetId
             * @return {{payload: {widgetId: string}}}
             */
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            /**
             * Сделать виджет видимым
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].visible = true
            },
        },

        HIDE: {
            /**
             * @param {string} widgetId
             * @return {{payload: {widgetId: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            /**
             * Сделать виджет невидимым
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].visible = false
            },
        },

        ENABLE: {
            /**
             * @param {string} widgetId
             * @return {{payload: {widgetId: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            /**
             * Сделать виджет разблокированым
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].disabled = false
            },
        },

        DISABLE: {
            /**
             * @param {string} widgetId
             * @return {{payload: {widgetId: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            /**
             * Сделать виджет заблокированым
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].disabled = true
            },
        },

        CHANGE_FILTER_VISIBILITY: {
            /**
             * @param {string} widgetId
             * @param {boolean} isFilterVisible
             * @return {{payload: WidgetsStore.changeFiltersVisibilityPayload}}
             */
            prepare(widgetId, isFilterVisible) {
                return ({
                    payload: { widgetId, isFilterVisible },
                })
            },

            /**
             * Изменить видимость фильтров виджета
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {WidgetsStore.changeFiltersVisibilityPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId, isFilterVisible } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isFilterVisible = isFilterVisible
            },
        },

        TOGGLE_FILTER_VISIBILITY: {
            /**
             * @param {string} widgetId
             * @return {{payload: {widgetId: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            /**
             * Изменить видимость фильтров виджета на противоположенную
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isFilterVisible = !state[widgetId].isFilterVisible
            },
        },

        RESET_STATE: {
            /**
             * @param {string} widgetId
             * @return {{payload: {widgetId: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            /**
             * Изменение поля isInit на значение false
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isInit = false
            },
        },

        SET_ACTIVE: {
            /**
             * @param {string} widgetId
             * @return {{payload: {widgetId: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            /**
             * Уставновить один активный виджет
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                Object.keys(state).forEach((widgetKey) => {
                    state[widgetKey].isActive = false
                })

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isActive = true
            },
        },

        REMOVE: {
            /**
             * @param {string} widgetId
             * @return {{payload: {widgetId: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            /**
             * Удалить виджет
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            reducer(state, { payload }) {
                delete state[payload.widgetId]
            },
        },
    },
})

export default widgetSlice.reducer

// Actions
export const {
    REGISTER: registerWidget,
    RESOLVE: resolveWidget,
    SHOW: showWidget,
    HIDE: hideWidget,
    ENABLE: enableWidget,
    DISABLE: disableWidget,
    CHANGE_FILTER_VISIBILITY: changeFiltersVisibility,
    RESET_STATE: resetWidgetState,
    TOGGLE_FILTER_VISIBILITY: toggleWidgetFilters,
    REMOVE: removeWidget,
    SET_ACTIVE: setActive,
} = widgetSlice.actions

/**
 * @param {string} widgetId
 * @param {string} alertKey
 * @return {{payload: WidgetsStore.alertAddWidgetPayload}}
 */
export const alertAddWidget = createAction(ALERT_ADD, (widgetId, alertKey) => ({
    payload: { widgetId, alertKey },
}))

/**
 * @param {string} widgetId
 * @param {string} alertKey
 * @return {{payload: WidgetsStore.alertRemoveWidgetPayload}}
 */
export const alertRemoveWidget = createAction(ALERT_REMOVE, (widgetId, alertKey) => ({
    payload: { widgetId, alertKey },
}))

/**
 * Показать филтры виджета
 * @param {string} widgetId
 */
export const showWidgetFilters = widgetId => changeFiltersVisibility(widgetId, true)

/**
 * Скрыть филтры виджета
 * @param {string} widgetId
 */
export const hideWidgetFilters = widgetId => changeFiltersVisibility(widgetId, false)

export const setWidgetMetadata = createAction(SET_WIDGET_METADATA, (pageId, widgetId, metadata) => ({
    payload: { pageId, widgetId, metadata },
}))
