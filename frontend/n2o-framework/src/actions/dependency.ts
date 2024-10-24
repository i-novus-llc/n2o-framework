import {
    REGISTER_DEPENDENCY,
} from '../constants/dependency'

import createActionHelper from './createActionHelper'

/**
 * Регистрация зависимости
 * @param widgetId
 * @param dependency
 */
export function registerDependency(widgetId: string, dependency: Record<string, unknown>) {
    return createActionHelper(REGISTER_DEPENDENCY)({ widgetId, dependency })
}
