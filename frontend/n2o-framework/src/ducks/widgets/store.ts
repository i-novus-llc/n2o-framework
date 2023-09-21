import { createSlice, createAction } from '@reduxjs/toolkit'
import set from 'lodash/set'

import WidgetResolver from './WidgetResolver'
import { ALERT_ADD, ALERT_REMOVE, SET_WIDGET_METADATA } from './constants'
import { State } from './Widgets'
import { ChangeFilterVisibility, Register, Resolve, Toggle } from './Actions'

const initialState: State = {}

const widgetSlice = createSlice({
    name: 'n2o/widgets',
    initialState,
    reducers: {
        REGISTER: {
            prepare(widgetId, initProps, preInit) {
                return ({
                    payload: { widgetId, initProps, preInit },
                })
            },

            reducer(state, action: Register) {
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
            prepare(widgetId, model, modelId) {
                return ({
                    payload: { widgetId, model, modelId },
                })
            },

            reducer(state, action: Resolve) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isResolved = true
            },
        },

        SHOW: {
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            reducer(state, action: Toggle) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].visible = true
            },
        },

        HIDE: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            reducer(state, action: Toggle) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].visible = false
            },
        },

        ENABLE: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            reducer(state, action: Toggle) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].disabled = false
            },
        },

        DISABLE: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            reducer(state, action: Toggle) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].disabled = true
            },
        },

        /**
         * Изменить видимость фильтров виджета
         */
        CHANGE_FILTER_VISIBILITY: {
            prepare(widgetId, isFilterVisible) {
                return ({
                    payload: { widgetId, isFilterVisible },
                })
            },

            reducer(state, action: ChangeFilterVisibility) {
                const { widgetId, isFilterVisible } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isFilterVisible = isFilterVisible
            },
        },

        /**
         * Изменить видимость фильтров виджета на противоположенную
         */
        TOGGLE_FILTER_VISIBILITY: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            reducer(state, action: Toggle) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isFilterVisible = !state[widgetId].isFilterVisible
            },
        },

        RESET_STATE: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            reducer(state, action: Toggle) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isInit = false
            },
        },

        /**
         * Уставновить один активный виджет
         */
        SET_ACTIVE: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            reducer(state, action: Toggle) {
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
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            reducer(state, action: Toggle) {
                const { payload } = action

                delete state[payload.widgetId]
            },
        },

        /**
         * Установить перенос текста в таблице
         */
        toggleTableWordWrap: {

            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId) {
                return ({
                    payload: { widgetId },
                })
            },

            reducer(state, action: Toggle) {
                const { payload } = action

                const { id } = payload

                if (!id) {
                    return
                }

                const { table } = state[id]

                if (!table) {
                    return
                }

                const { textWrap } = table

                set(state, `${id}.table.textWrap`, !textWrap)
            },
        },
    },
})

export default widgetSlice.reducer

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
    toggleTableWordWrap,
} = widgetSlice.actions

export const alertAddWidget = createAction(ALERT_ADD, (widgetId: string, alertKey: string) => ({
    payload: { widgetId, alertKey },
}))

export const alertRemoveWidget = createAction(ALERT_REMOVE, (widgetId: string, alertKey: string) => ({
    payload: { widgetId, alertKey },
}))

export const showWidgetFilters = (widgetId: string) => changeFiltersVisibility(widgetId, true)

export const hideWidgetFilters = (widgetId: string) => changeFiltersVisibility(widgetId, false)

export const setWidgetMetadata = createAction(
    SET_WIDGET_METADATA, (pageId: string, widgetId: string, metadata: object) => ({
        payload: { pageId, widgetId, metadata },
    }),
)
