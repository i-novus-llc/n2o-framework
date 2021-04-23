import { createSelector } from 'reselect'

/**
 * Селектор глоабльных настроек
 */
export const globalSelector = state => state.global || {}

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

export const getLocales = createSelector(
    globalSelector,
    global => global.locales || {},
)
