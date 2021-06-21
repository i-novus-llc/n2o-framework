import { createSelector } from '@reduxjs/toolkit'

/*
 селектор для всех кнопок
 */

/**
 * @param {Object.<string, any>} state
 * @return {Toolbar.store}
 */
export const toolbarSelector = state => state.toolbar || {}

/**
 * @param {string} containerKey
 * @return {Object.<string, any>}
 */
export const getContainerButtons = containerKey => createSelector(
    toolbarSelector,
    toolbar => toolbar[containerKey] || {},
)

/**
 * селектор для кнопки по уникальному ключу
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {Object.<string, any>}
 */
export const makeButtonByKeyAndIdSelector = (key, id) => createSelector(
    getContainerButtons(key),
    containerButtons => containerButtons[id] || {},
)

/**
 * селектор для того, чтобы узнать зарегистрирована кнопка или нет
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {boolean}
 */
export const isInitSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.isInit,
)

/**
 *  селектор видимости кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {boolean}
 */
export const isVisibleSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.visible,
)

/**
 * селектор блокировки кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {boolean}
 */
export const isDisabledSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.disabled,
)

/**
 * селектор пазмера кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {string}
 */
export const sizeSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.size,
)

/**
 * селектор цвета кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {string}
 */
export const colorSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.color,
)

/**
 * селектор счетчика кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {number}
 */
export const countSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.count,
)

/**
 * селектор имени кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {string}
 */
export const titleSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.title,
)

/**
 * селектор посказаки кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {string}
 */
export const hintSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.hint,
)

/**
 * селектор посказаки кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {any}
 */
export const messageSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.message,
)

/**
 * селектор расположения посказаки кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {any}
 */

export const hintPositionSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.hintPosition,
)

/**
 * селектор иконки кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {string}
 */
export const iconSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.icon,
)

/**
 * селектор стиля кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {CSSStyleDeclaration}
 */
export const styleSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.style,
)

/**
 * селектор класса кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {string}
 */
export const classSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.className,
)

/**
 * селектор выполнения
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {boolean}
 */
export const isLoading = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.loading,
)
/**
 * селектор ошибок экшена кнопки
 * @param {string} key - ключ в сторе
 * @param {string} id - id кнопки
 * @return {any}
 */
export const errorSelector = (key, id) => createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => button.error,
)
