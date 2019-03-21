import { START_DEPENDENCY, REGISTER_DEPENDENCY } from '../constants/dependency';

import createActionHelper from './createActionHelper';

/**
 * Регистрация зависимости
 * @param widgetId
 * @param options
 */
export function registerDependency(widgetId, options) {
  return createActionHelper(REGISTER_DEPENDENCY)({ widgetId, options });
}

export function startDependency(dependency, values, widget, fieldName) {
  return createActionHelper(START_DEPENDENCY)({ dependency, values, widget, fieldName });
}
