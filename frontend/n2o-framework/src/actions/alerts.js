import { ADD, ADD_MULTI, REMOVE, REMOVE_ALL } from '../constants/alerts'
import { id } from '../utils/id'

import createActionHelper from './createActionHelper'

/**
 * Добавить предупреждения
 * @param key
 * @param alerts
 */
export function addAlert(
    key,
    { severity, label, text, details, timeout, closeButton },
) {
    return createActionHelper(ADD)({
        severity,
        label,
        text,
        details,
        timeout,
        closeButton,
        id: id(),
        key,
    })
}

/**
 * Добавляет массив предупреждений
 * @param key
 * @param alerts
 */
export function addAlerts(key, alerts) {
    return createActionHelper(ADD_MULTI)({ key, alerts })
}

/**
 * Удалить предупреждение
 * @param key
 * @param id
 */
export function removeAlert(key, id) {
    return createActionHelper(REMOVE)({ id, key })
}

/**
 * Удалить все предупреждения по ключу
 * @param key
 */
export function removeAlerts(key) {
    return createActionHelper(REMOVE_ALL)({ key })
}

/*
function concatAlerts(widgetId, state, alert) {
  const widgetState = state.widgets[widgetId];
  return _.concat(widgetState.alerts, [alert]);
}

export function widgetAlertConcat(widgetId, alert) {
  return (dispatch, getState) => {
    return dispatch(widgetAlertAdd(widgetId, concatAlerts(widgetId, getState(), alert)));
  };
}
*/
