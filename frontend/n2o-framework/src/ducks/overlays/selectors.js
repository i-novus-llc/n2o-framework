import { createSelector } from '@reduxjs/toolkit'

/**
 * селектор модольных окон
 * @param {Object.<string, any>} state
 * @return TOverlayState
 */
export const overlaysSelector = state => state.overlays || []

/**
 * селектор модального окна по индексу
 */
export const makeOverlayByIndex = index => createSelector(
    [
        overlaysSelector,
    ],
    overlaysState => overlaysState[index],
)

/**
 * Получение оверлей по имени
 */
const makeOverlayByName = name => createSelector(
    [
        overlaysSelector,
    ],
    overlaysState => overlaysState.find(overlay => overlay.name === name),
)

/**
 */
export const makeShowPromptByName = name => createSelector(
    makeOverlayByName(name),
    overlay => Boolean(overlay?.showPrompt),
)
