// TODO: Дописать тесты которых не хватает, если таковые имеются
import { createSlice, createSelector } from '@reduxjs/toolkit'

import { id } from '../../utils/id'

export const initialState = {}

const alertsSlice = createSlice({
    name: 'n2o/alerts',
    initialState,
    reducers: {

        ADD: {
            /**
             * @param {string} alertStoreKey
             * @param {AlertsStore.item} alert
             * @return {{payload: AlertsStore.addPayload}}
             */
            prepare(alertStoreKey, alert) {
                return ({
                    payload: { key: alertStoreKey, alert },
                })
            },

            /**
             * Добавление алерта в стор
             * @param {AlertsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {AlertsStore.addPayload} action.payload
             */
            reducer(state, action) {
                const { key, alert } = action.payload

                const resolveAlert = { ...alert, id: id() }

                if (!state[key]) {
                    state[key] = []
                }

                state[key].push(resolveAlert)
            },
        },

        ADD_MULTI: {

            /**
             * @param {string} alertStoreKey
             * @param {AlertsStore.item[]} alerts
             * @return {{payload: AlertsStore.addMultiPayload}}
             */
            prepare(alertStoreKey, alerts) {
                return ({
                    payload: { key: alertStoreKey, alerts },
                })
            },

            /**
             * Добавление алертов в стор
             * @param {AlertsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {AlertsStore.addMultiPayload} action.payload
             */
            reducer(state, action) {
                const { key, alerts } = action.payload

                if (state[key]) {
                    state[key] = state[key].concat(alerts)
                } else {
                    state[key] = alerts
                }
            },
        },

        REMOVE: {
            /**
             * @param {string} alertStoreKey
             * @param {string} alertId
             * @return {{payload: AlertsStore.removePayload}}
             */
            prepare(alertStoreKey, alertId) {
                return ({
                    payload: { key: alertStoreKey, id: alertId },
                })
            },

            /**
             * Удаление алерта по id из стора алертов по ключу(widgetId)
             * @param {AlertsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {AlertsStore.removePayload} action.payload
             */
            reducer(state, action) {
                const { key, id } = action.payload

                if (!state[key] || !state[key].length) {
                    return
                }

                const filtered = state[key].filter(alert => alert.id !== id)

                if (filtered.length) {
                    state[key] = filtered
                } else {
                    delete state[key]
                }
            },
        },

        /**
         * Удаление алертов алертов по ключу(widgetId)
         * @param {AlertsStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {AlertsStore.removeAllPayload} action.payload
         */
        REMOVE_ALL(state, action) {
            if (!state[action.payload]) {
                return
            }

            delete state[action.payload]
        },
    },
})

// Selectors
/**
 * Селектор алертов
 * @param {Object} store
 * @return {AlertsStore.state}
 */
const alertsSelector = store => store.alerts

/**
 * Селектор айтемов по ключу(widgetId)
 * @param {string} key
 * @returns {AlertsStore.item[]}
 */
export const alertsByKeySelector = key => createSelector(
    alertsSelector,
    alertsStore => alertsStore[key] || [],
)

/**
 * Селектор айтема по ключу(widgetId) и id алерта
 * @param {string} key
 * @param {string} id
 * @returns {AlertsStore.item | null}
 */
export const alertByIdAndKeySelector = (key, id) => createSelector(
    alertsByKeySelector(key),
    alerts => (alerts.length ? alerts[id] : null),
)

// Actions
export const {
    ADD: addAlert,
    ADD_MULTI: addMultiAlerts,
    REMOVE: removeAlert,
    REMOVE_ALL: removeAllAlerts,
} = alertsSlice.actions
export default alertsSlice.reducer
