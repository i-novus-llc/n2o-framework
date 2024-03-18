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
