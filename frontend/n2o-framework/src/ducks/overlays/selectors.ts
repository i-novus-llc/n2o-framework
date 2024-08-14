import { createSelector } from '@reduxjs/toolkit'

import { State } from '../State'

/**
 * селектор модольных окон
 */
export const overlaysSelector = (state: State) => state.overlays || []

/**
 * селектор модального окна по индексу
 */
export const makeOverlayByIndex = (index: number) => createSelector(
    [
        overlaysSelector,
    ],
    overlaysState => overlaysState[index],
)

/**
 * Получение оверлей по имени
 */
const makeOverlayByName = (name: string) => createSelector([overlaysSelector], overlaysState => overlaysState.find(overlay => overlay.name === name))

/**
 * Получение showPrompt по имени
 */
export const makeShowPromptByName = (name: string) => createSelector(
    makeOverlayByName(name),
    overlay => Boolean(overlay?.showPrompt),
)
