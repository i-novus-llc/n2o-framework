import {
    REGISTER_DEPENDENCY,
    UPDATE_WIDGET_DEPENDENCY,
} from '../constants/dependency'

import createActionHelper from './createActionHelper'

/**
 * Регистрация зависимости
 * @param widgetId
 * @param dependency
 */
export function registerDependency(widgetId, dependency) {
    return createActionHelper(REGISTER_DEPENDENCY)({ widgetId, dependency })
}

/**
 * Запуск зависимости
 * @param widgetId
 */
export function updateWidgetDependency(widgetId) {
    return createActionHelper(UPDATE_WIDGET_DEPENDENCY)({ widgetId })
}
