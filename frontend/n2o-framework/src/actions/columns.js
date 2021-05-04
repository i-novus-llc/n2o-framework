import {
    CHANGE_COLUMN_DISABLED,
    CHANGE_COLUMN_VISIBILITY,
    CHANGE_FROZEN_COLUMN,
    REGISTER_COLUMN,
    TOGGLE_COLUMN_VISIBILITY,
} from '../constants/columns'

import createActionHelper from './createActionHelper'

/**
 * Изменение видимсоти колонки таблицы
 * @param widgetId
 * @param columnId
 * @param visible
 */
export function changeColumnVisiblity(widgetId, columnId, visible) {
    return createActionHelper(CHANGE_COLUMN_VISIBILITY)({
        visible,
        key: widgetId,
        columnId,
    })
}

/**
 * Сделать колонку видимой
 * @param widgetId
 * @param columnId
 */
export function setColumnVisible(widgetId, columnId) {
    return changeColumnVisiblity(widgetId, columnId, true)
}

/**
 * Скрыть колонку
 * @param widgetId
 * @param columnId
 * @returns {*}
 */
export function setColumnHidden(widgetId, columnId) {
    return changeColumnVisiblity(widgetId, columnId, false)
}

/**
 * Поменять видимость колонки на противоположенную
 * @param widgetId
 * @param columnId
 * @returns {*}
 */
export function toggleColumnVisiblity(widgetId, columnId) {
    return createActionHelper(TOGGLE_COLUMN_VISIBILITY)({
        key: widgetId,
        columnId,
    })
}

export function changeColumnDisabled(widgetId, columnId, disabled) {
    return createActionHelper(CHANGE_COLUMN_DISABLED)({
        key: widgetId,
        columnId,
        disabled,
    })
}

/**
 * Зарегистрировать колонку в редаксе
 * @param widgetId
 * @param columnId
 * @param label
 * @param disabled
 * @param visible
 * @param conditions
 * @returns {*}
 */
export function registerColumn(
    widgetId,
    columnId,
    label,
    visible,
    disabled,
    conditions,
) {
    return createActionHelper(REGISTER_COLUMN)({
        key: widgetId,
        columnId,
        label,
        visible,
        disabled,
        conditions,
    })
}

export function changeFrozenColumn(widgetId, columnId) {
    return createActionHelper(CHANGE_FROZEN_COLUMN)({
        key: widgetId,
        columnId,
    })
}
