import { createSlice } from '@reduxjs/toolkit'
import { LOCATION_CHANGE } from 'connected-react-router'

import { State as Global } from './Global'

export const initialState: Global = {
    ready: false,
    loading: false,
    error: null,
    locale: 'ru',
    messages: {},
    menu: {},
    rootPageId: null,
    breadcrumbs: {},
}

export const globalSlice = createSlice({
    name: 'n2o/global',
    initialState,
    reducers: {
        CHANGE_LOCALE(state: Global, action) {
            state.locale = action.payload
        },

        REQUEST_CONFIG(state) {
            state.loading = true
        },

        REQUEST_CONFIG_SUCCESS(state, action) {
            state = Object.assign(state, action.payload)
        },

        SET_GLOBAL_LOADING(state, { payload }) {
            state.loading = payload
        },

        REQUEST_CONFIG_FAIL: {
            prepare(errorMessage) {
                return {
                    payload: errorMessage,
                    meta: {
                        alert: errorMessage,
                    },
                }
            },

            reducer(state: Global, action: { payload: object }) {
                state.loading = false
                state.error = action.payload
            },
        },

        CHANGE_ROOT_PAGE(state: Global, action) {
            state.rootPageId = action.payload
        },

        SET_READY(state) {
            state.ready = true
        },

        REGISTER_LOCALES(state: Global, action) {
            state.locales = action.payload
        },
    },
    extraReducers: {
        /**
         * Отслеживание изменения адреса для восстановления значения фильтров/табов при переходе по хлебным крошкам назад
         */
        [LOCATION_CHANGE](state, { payload }) {
            const { breadcrumbs } = state
            const { location } = payload
            const { pathname, search } = location
            const routes: Record<string, string> = {}

            for (const [path, query] of Object.entries(breadcrumbs)) {
                if (pathname.startsWith(path)) {
                    // step in or replace query
                    routes[path] = query
                }
                // else = step out or replace path
            }

            routes[pathname] = search

            state.breadcrumbs = routes
        },
    },
})

export default globalSlice.reducer

export const {
    CHANGE_LOCALE: changeLocale,
    CHANGE_ROOT_PAGE: changeRootPage,
    REGISTER_LOCALES: registerLocales,
    REQUEST_CONFIG: requestConfig,
    REQUEST_CONFIG_FAIL: requestConfigFail,
    REQUEST_CONFIG_SUCCESS: requestConfigSuccess,
    SET_READY: setReady,
    SET_GLOBAL_LOADING: setGlobalLoading,
} = globalSlice.actions
