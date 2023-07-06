import { createSlice } from '@reduxjs/toolkit'
import take from 'lodash/take'

import { ALLOWED_ALERTS_QUANTITY } from './constants'
import { State } from './Alerts'
import { Add, AddMulti, Remove, RemoveAll, StopRemoving } from './Actions'

const initialState: State = {}

const alertsSlice = createSlice({
    name: 'n2o/alerts',
    initialState,
    reducers: {
        ADD: {
            prepare(alertStoreKey, alert) {
                return ({
                    payload: { key: alertStoreKey, alert },
                })
            },
            reducer(state, action: Add) {
                const { key, alert } = action.payload

                if (!state[key]) {
                    state[key] = []
                }

                state[key]?.unshift(alert)
                state[key] = take(state[key], ALLOWED_ALERTS_QUANTITY)
            },
        },

        ADD_MULTI: {
            prepare(alertStoreKey, alerts) {
                return ({
                    payload: { key: alertStoreKey, alerts },
                })
            },
            reducer(state, action: AddMulti) {
                const { key, alerts } = action.payload

                if (!state[key]) {
                    state[key] = []
                }

                state[key]?.unshift(...alerts)
                state[key] = take(state[key], ALLOWED_ALERTS_QUANTITY)
            },
        },

        REMOVE: {
            prepare(alertStoreKey, alertId) {
                return ({
                    payload: { key: alertStoreKey, id: alertId },
                })
            },
            reducer(state, action: Remove) {
                const { key, id } = action.payload

                const alerts = state[key]

                if (!alerts || !alerts.length) {
                    return
                }

                const filtered = alerts.filter(alert => alert.id !== id)

                if (filtered.length) {
                    state[key] = filtered
                } else {
                    delete state[key]
                }
            },
        },

        REMOVE_ALL: {
            prepare(key) {
                return ({
                    payload: key,
                })
            },
            reducer(state, action: RemoveAll) {
                const { payload } = action
                const { key } = payload

                if (!state[key]) {
                    return
                }
                delete state[key]
            },
        },

        STOP_REMOVING: {
            prepare(key, id) {
                return ({
                    payload: { key, id },
                })
            },
            reducer(state, action: StopRemoving) {
                const { key, id } = action.payload

                state[key] = state[key]?.map((alert) => {
                    if (alert.id === id) {
                        return { ...alert, stopped: true }
                    }

                    return alert
                })
            },
        },
    },
})

export const {
    ADD: addAlert,
    ADD_MULTI: addMultiAlerts,
    REMOVE: removeAlert,
    REMOVE_ALL: removeAllAlerts,
    STOP_REMOVING: stopRemovingAlert,
} = alertsSlice.actions

export default alertsSlice.reducer
