import { createSelector } from '@reduxjs/toolkit'

import { State as ReduxState } from '../State'
import { EMPTY_ARRAY } from '../../utils/emptyTypes'

import { KeyType } from './constants'

/**
 * Селектор алертов
 */
const alertsSelector = (store: ReduxState) => store.alerts

/**
 * Селектор айтемов по ключу
 */
export const alertsByKeySelector = (key: KeyType) => createSelector(
    alertsSelector,
    alertsStore => alertsStore[key] || EMPTY_ARRAY,
)

/**
 * Селектор по ключу и индексу алерта
 */
export const alertByIdAndKeySelector = (key: KeyType, index: number) => createSelector(
    alertsByKeySelector(key),
    alerts => (alerts.length ? alerts[index] : null),
)
