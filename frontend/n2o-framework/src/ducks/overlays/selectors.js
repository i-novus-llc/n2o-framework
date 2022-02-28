import { createSelector } from '@reduxjs/toolkit'
import find from 'lodash/find'

/**
 * селектор модольных окон
 * @param {Object.<string, any>} state
 */
export const overlaysSelector = state => state.overlays || {}

/**
 * селектор модального окна по индексу
 * @param {number} index
 */
export const makeOverlaysbyName = index => createSelector(
    overlaysSelector,
    overlaysState => overlaysState[index],
)

export const makeShowPromptByName = name => createSelector(
    overlaysSelector,
    overlaysState => find(overlaysState, overlay => overlay.name === name)?.showPrompt,
)
