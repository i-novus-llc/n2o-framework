import { START_DEPENDENCY } from '../constants/dependency';

import createActionHelper from './createActionHelper';

export function startDependency(dependency, values, widget, fieldName) {
  return createActionHelper(START_DEPENDENCY)({ dependency, values, widget, fieldName });
}
