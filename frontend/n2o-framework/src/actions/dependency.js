import {
    REGISTER_DEPENDENCY,
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
