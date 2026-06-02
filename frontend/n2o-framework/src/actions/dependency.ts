import {
    REGISTER_DEPENDENCY,
} from '../constants/dependency'
import { type Action } from '../ducks/Action'
import { type WidgetDependencies } from '../sagas/widgetDependency/WidgetTypes'

import createActionHelper from './createActionHelper'

export type RegisterDependency = Action<string, WidgetDependencies>

/**
 * Регистрация зависимости
 * @param widgetId
 * @param dependency
 */
export function registerDependency(
    widgetId: WidgetDependencies['widgetId'],
    dependency?: WidgetDependencies['dependency'],
) {
    return createActionHelper(REGISTER_DEPENDENCY)({ widgetId, dependency })
}
