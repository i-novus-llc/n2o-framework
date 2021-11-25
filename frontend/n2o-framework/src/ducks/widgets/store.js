import { createSlice, createAction } from '@reduxjs/toolkit'
import isEmpty from 'lodash/isEmpty'

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
                let smartState = {}
                const currentState = state[widgetId] || {}

                if (!isEmpty(currentState)) {
                    smartState = {
                        selectedId: currentState.selectedId ? currentState.selectedId : null,
                    }

                    if (!isEmpty(currentState.sorting)) {
                        smartState.sorting = currentState.sorting
                    }
                }

                state[widgetId] = {
                    ...WidgetResolver.defaultState,
                    ...currentState,
                    ...initProps,
                    ...smartState,
                    modelId: initProps.modelId || widgetId,
                    isInit: !preInit,
                    type: initProps.type,
                }
            },
        },

        DATA_REQUEST: {
            /**
             * @param {string} widgetId
             * @param {string} modelId
             * @param {object} options
             * @return {{payload: WidgetsStore.dataRequestWidgetPayload}}
             */
            prepare(widgetId, modelId, options = {}) {
                return ({
                    payload: { widgetId, options, modelId },
                })
            },

            /**
             * Запрос за данными
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {Columns.dataRequestWidgetPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isLoading = true
            },
        },

        DATA_SUCCESS: {
            /**
             * @param {string} widgetId
             * @param {object} json
             * @return {{payload: WidgetsStore.dataSuccessWidgetPayload, meta: object}}
             */
            prepare(widgetId, json) {
                return ({
                    payload: { widgetId, query: json },
                    meta: json.meta,
                })
            },

            /**
             * Вспомогательный экшен. Успешный запрос за данными
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {Columns.dataSuccessWidgetPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isLoading = false
            },
        },

        DATA_FAIL: {
            /**
             * @param {string} widgetId
             * @param {object} err
             * @param {object} meta
             * @return {{payload: WidgetsStore.dataFailWidgetPayload, meta: object}}
             */
            prepare(widgetId, err, meta) {
                return ({
                    payload: { widgetId, err },
                    meta,
                })
            },

            /**
             * Вспомогательный экшен. Ошибка при запросе за данными.
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {Columns.dataFailWidgetPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId, err = true } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isLoading = false
                state[widgetId].error = err
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

                state[widgetId].isVisible = true
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

                state[widgetId].isVisible = false
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

                state[widgetId].isEnabled = true
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

                state[widgetId].isEnabled = false
            },
        },

        DISABLE_ON_FETCH: {
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
            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isEnabled = false
            },
        },

        LOADING: {
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
             * Активировать анимацию загрузки у виджета
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isLoading = true
            },
        },

        UNLOADING: {
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
             * Деактивировать анимацию загрузки у виджета
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {{widgetId: string}} action.payload
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action) {
                const { widgetId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].isLoading = false
            },
        },

        SORT_BY: {
            /**
             * @param {string} widgetId
             * @param {string} fieldKey
             * @param {string} sortDirection
             * @return {{payload: WidgetsStore.sortByWidgetPayload}}
             */
            prepare(widgetId, fieldKey, sortDirection) {
                return ({
                    payload: { widgetId, fieldKey, sortDirection },
                })
            },

            /**
             * Соритровка по полю
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {WidgetsStore.sortByWidgetPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId, fieldKey, sortDirection } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                if (sortDirection === 'NONE') {
                    state[widgetId].sorting = {}
                } else {
                    state[widgetId].sorting = { [fieldKey]: sortDirection }
                }
            },
        },

        CHANGE_PAGE: {
            /**
             * @param {string} widgetId
             * @param {string} page
             * @return {{payload: WidgetsStore.changePageWidgetPayload}}
             */
            prepare(widgetId, page) {
                return ({
                    payload: { widgetId, page },
                })
            },

            /**
             * Меняет номер страницы виджета
             * Этот параметр используется при запросах на сервер
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {WidgetsStore.changePageWidgetPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId, page } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].page = page
            },
        },

        CHANGE_COUNT: {
            /**
             * @param {string} widgetId
             * @param {number} count
             * @return {{payload: WidgetsStore.changeCountWidgetPayload}}
             */
            prepare(widgetId, count) {
                return ({
                    payload: { widgetId, count },
                })
            },

            /**
             * Меняет номер страницы виджета
             * Этот параметр используется при запросах на сервер
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {WidgetsStore.changeCountWidgetPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId, count } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].count = count
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

        CHANGE_SELECTED_ID: {
            /**
             * @param {string} widgetId
             * @param {string} selectedId
             * @return {{payload: WidgetsStore.setTableSelectedIdPayload}}
             */
            prepare(widgetId, selectedId) {
                return ({
                    payload: { widgetId, value: selectedId },
                })
            },

            /**
             * Изменить выбранную запись в виджете
             * @param {WidgetsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {WidgetsStore.setTableSelectedIdPayload} action.payload
             */
            reducer(state, action) {
                const { widgetId, value } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = WidgetResolver.defaultState
                }

                state[widgetId].selectedId = WidgetResolver.resolveSelectedId(value)
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
            reducer(state, action) {
                delete state[action.payload]
            },
        },
    },
})

export default widgetSlice.reducer

// Actions
export const {
    REGISTER: registerWidget,
    DATA_REQUEST: dataRequestWidget,
    DATA_SUCCESS: dataSuccessWidget,
    DATA_FAIL: dataFailWidget,
    RESOLVE: resolveWidget,
    SHOW: showWidget,
    HIDE: hideWidget,
    ENABLE: enableWidget,
    DISABLE: disableWidget,
    DISABLE_ON_FETCH: disableWidgetOnFetch,
    LOADING: loadingWidget,
    UNLOADING: unloadingWidget,
    SORT_BY: sortByWidget,
    CHANGE_PAGE: changePageWidget,
    CHANGE_COUNT: changeCountWidget,
    CHANGE_FILTER_VISIBILITY: changeFiltersVisibility,
    CHANGE_SELECTED_ID: setTableSelectedId,
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
