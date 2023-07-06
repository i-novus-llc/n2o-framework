import { createSelector } from '@reduxjs/toolkit'

import { State } from '../State'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

/**
 * Получить slice toolbar'а
 */
export const toolbarSelector = (state: State) => state.toolbar

/**
 * Получение контейнера по id
 */
export const getContainerButtons = (containerId: string) => createSelector(
    [
        toolbarSelector,
    ],
    toolbar => toolbar[containerId] || EMPTY_OBJECT,
)

/**
 * Получить контейнер с кнопками
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId id контейнера кнопок
 * @return {Object.<string, any>}
 */
const buttonsContainerSelector = (
    state: State,
    containerId: string,
) => toolbarSelector(state)[containerId] || EMPTY_OBJECT

/**
 * Получить кнопку
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {Object.<string, any>}
 */
export const buttonSelector = (state: State, containerId: string, buttonId: string) => (
    buttonsContainerSelector(state, containerId)[buttonId] || EMPTY_OBJECT
)

/**
 * Селектор зарегистрирована ли кнопка
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {boolean}
 */
export const isInitSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).isInit

/**
 * Селектор видимости
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {boolean}
 */
export const isVisibleSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).visible

/**
 * Селектор заблокирована ли кнопка
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {boolean}
 */
export const isDisabledSelector = (state: State, containerId: string, buttonId: string) => (
    buttonSelector(state, containerId, buttonId).disabled
)

/**
 * Селектор размера
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const sizeSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).size

/**
 * Селектор цвета
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const colorSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).color

/**
 * Селектор счетчика
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {number}
 */
export const countSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).count

/**
 * Селектор тайтла
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const titleSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).title

/**
 * Селектор подсказки кнопки
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const hintSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).hint

/**
 * Селектор сообщения
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {any}
 */
export const messageSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).message

/**
 * Селектор расположения подсказки
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {any}
 */

export const hintPositionSelector = (state: State, containerId: string, buttonId: string) => (
    buttonSelector(state, containerId, buttonId).hintPosition
)

/**
 * Селектор иконки
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const iconSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).icon

/**
 * Селектор стиля
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {CSSStyleDeclaration}
 */
export const styleSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).style

/**
 * Селектор css-класса
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {string}
 */
export const classSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).className

/**
 * Селектор загружаются ли данные
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {boolean}
 */
export const isLoading = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).loading

/**
 * Селектор ошибки кнопки
 * @param {Object.<string, any>} state стейт стора
 * @param {string} containerId - id контейнера кнопок
 * @param {string} buttonId - id кнопки
 * @return {any}
 */
export const errorSelector = (
    state: State,
    containerId: string,
    buttonId: string,
) => buttonSelector(state, containerId, buttonId).error
