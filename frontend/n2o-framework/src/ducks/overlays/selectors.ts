import { createSelector } from '@reduxjs/toolkit'

import { State } from '../State'

/**
 * селектор модольных окон
 */
export const overlaysSelector = (state: State) => state.overlays || []

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
