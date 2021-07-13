import { createSelector } from 'reselect'

/**
 * Получить slice toolbar'а
 * @param {Object.<string, any>} state стейт стора
 * @return {Toolbar.store}
 */
export const toolbarSelector = state => state.toolbar

// ToDo: Разобраться с сагой. Как то странно она работает
export const getContainerButtons = containerKey => createSelector(
    toolbarSelector,
    toolbar => toolbar[containerKey] || {},
)

/**
 * Получить контейнер с кнопками
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId id контейнера кнопок
 * @return {Object.<string, any>}
 */
const buttonsContainerSelector = (state, containerId) => toolbarSelector(state)[containerId] || {}

/**
 * Получить кнопку
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {Object.<string, any>}
 */
export const buttonSelector = (state, containerId, buttonId) => (
    buttonsContainerSelector(state, containerId)[buttonId] || {}
)

/**
 * Селектор зарегистрирована ли кнопка
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {boolean}
 */
export const isInitSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).isInit

/**
 * Селектор видимости
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {boolean}
 */
export const isVisibleSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).visible

/**
 * Селектор заблокирована ли кнопка
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {boolean}
 */
export const isDisabledSelector = (state, containerId, buttonId) => (
    buttonSelector(state, containerId, buttonId).disabled
)

/**
 * Селектор размера
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const sizeSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).size

/**
 * Селектор цвета
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const colorSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).color

/**
 * Селектор счетчика
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {number}
 */
export const countSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).count

/**
 * Селектор тайтла
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const titleSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).title

/**
 * Селектор подсказки кнопки
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const hintSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).hint

/**
 * Селектор сообщения
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {any}
 */
export const messageSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).message

/**
 * Селектор расположения подсказки
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {any}
 */

export const hintPositionSelector = (state, containerId, buttonId) => (
    buttonSelector(state, containerId, buttonId).hintPosition
)

/**
 * Селектор иконки
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const iconSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).icon

/**
 * Селектор стиля
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {CSSStyleDeclaration}
 */
export const styleSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).style

/**
 * Селектор css-класса
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const classSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).className

/**
 * Селектор загружаются ли данные
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {boolean}
 */
export const isLoading = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).loading

/**
 * Селектор ошибки кнопки
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {any}
 */
export const errorSelector = (state, containerId, buttonId) => buttonSelector(state, containerId, buttonId).error
