import { createSlice, createSelector } from '@reduxjs/toolkit'
import { LOCATION_CHANGE } from 'connected-react-router'
import get from 'lodash/get'

const initialState = {
    ready: false,
    loading: false,
    error: null,
    locale: 'ru',
    messages: {},
    menu: {},
    rootPageId: null,
    breadcrumbs: {},
}
const globalSlice = createSlice({
    name: 'n2o/global',
    initialState,
    reducers: {
        /**
         * Установка локализации
         * @param {GlobalStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
        CHANGE_LOCALE(state, action) {
            state.locale = action.payload
        },

        /**
         * Установка флага loading в значение true
         * @param {GlobalStore.state} state
         */
        REQUEST_CONFIG(state) {
            state.loading = true
        },

        /**
         * Установка конфига при успешной его загрузке
         * @param {GlobalStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {Object.<string, any>} action.payload
         */
        REQUEST_CONFIG_SUCCESS(state, action) {
            state = Object.assign(state, action.payload)
        },

        SET_GLOBAL_LOADING(state, { payload }) {
            state.loading = payload
        },

        REQUEST_CONFIG_FAIL: {

            /**
             * @param {Object.<string, any>} errorMessage
             * @return Global.requestConfigFail
             */
            prepare(errorMessage) {
                return {
                    payload: errorMessage,
                    meta: {
                        alert: errorMessage,
                    },
                }
            },

            /**
             * Установка ошибки при ошибке загрузки config
             * @param {GlobalStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {Global.requestConfigFail.payload} action.payload
             */
            reducer(state, action) {
                state.loading = false
                state.error = action.payload
            },
        },

        /**
         * Изменение поля rootPageId
         * @param {GlobalStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
        CHANGE_ROOT_PAGE(state, action) {
            state.rootPageId = action.payload
        },

        /**
         * Установка флага ready в значение true
         * @param {GlobalStore.state} state
         */
        SET_READY(state) {
            state.ready = true
        },

        /**
         * Изменение поля locales
         * @param {GlobalStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string[]} action.payload
         */
        REGISTER_LOCALES(state, action) {
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
            const routes = {}

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

// Selectors
/**
 * Селектор глоабльных настроек
 */
export const globalSelector = state => get(state, 'global', {})

/**
 * Селектор текущей локализации
 */
export const localeSelector = createSelector(
    globalSelector,
    global => global.locale,
)

/**
 * Селектор состояния загрузки приложения
 */
export const appLoadingSelector = createSelector(
    globalSelector,
    global => global.loading,
)

/**
 * Селектор конфига сообщений для локализации
 */
export const localizationSelector = createSelector(
    globalSelector,
    global => global.messages,
)

/**
 * Селектор данных для навигационого меню (header, sidebar)
 */
export const menuSelector = createSelector(
    globalSelector,
    global => global.menu,
)

/**
 * Селектор данных пользователя из конфига
 */
export const userSelector = createSelector(
    globalSelector,
    global => global.menu,
)

/**
 * Селектор данных пользователя из конфига
 */
export const userConfigSelector = createSelector(
    globalSelector,
    global => global.user,
)

/**
 * Селектор глобальной ошибки приложения
 */
export const errorSelector = createSelector(
    globalSelector,
    global => global.error,
)

/**
 * Селектор текущей страницы
 */
export const rootPageSelector = createSelector(
    globalSelector,
    global => global.rootPageId,
)

/**
 * Селектор текущей страницы
 */
export const readySelector = createSelector(
    globalSelector,
    global => global.ready,
)

export const breadcrumbsSelector = createSelector(
    globalSelector,
    global => global.breadcrumbs || {},
)

/**
 * Селектор роутинга
 */
export const routerSelector = state => state.router || {}

/**
 * Селектор location объекта
 */
export const getLocation = createSelector(
    routerSelector,
    router => router.location,
)

/**
 * Селектор locales объекта
 */
export const getLocales = createSelector(
    globalSelector,
    global => global.locales || {},
)

/**
 * Селектор url для редиректа по 401
 */
export const getAuthUrl = createSelector(
    globalSelector,
    global => global.redirectPath,
)
