import { createSelector } from 'reselect'
import find from 'lodash/find'

/**
 * селектор модольных окон
 * @param state
 */
export const overlaysSelector = state => state.overlays || {}

/**
 * селектор модального окна по индексу
 * @param i
 */
export const makeOverlaysbyName = i => createSelector(
    overlaysSelector,
    overlaysState => overlaysState[i],
)

export const makeShowPromptByName = name => createSelector(
    overlaysSelector,
    overlaysState => find(overlaysState, overlay => overlay.name === name).showPrompt,
)
