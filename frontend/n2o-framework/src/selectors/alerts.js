import { createSelector } from 'reselect'

/**
 * Селектор всех алертов
 */
export const alertsSelector = state => state.alerts || {}
/**
 * Селектор алертов по ключу(widgetId)
 */
export const makeAlertsByKeySelector = key => createSelector(
    alertsSelector,
    alerts => alerts[key],
)
/**
 * Селектор алерта по ключу(widgetId) и id алерта
 */
export const makeAlertByKeyAndIdSelector = (key, id) => createSelector(
    makeAlertByKeyAndIdSelector(key),
    alerts => alerts[id],
)
