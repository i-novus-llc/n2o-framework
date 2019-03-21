import { select, fork, actionChannel, take, call } from 'redux-saga/effects';
import { isEmpty, keys } from 'lodash';
import { REGISTER_DEPENDENCY } from '../constants/dependency';
import { SET } from '../constants/models';

/**
 * Добавляет в хранилище новопришедший виджет с его widgetId и dependency
 * @param widgetsDependencies
 * @param widgetId
 * @param dependency
 * @returns {{}}
 */
export function* registerWidgetDependency(widgetsDependencies, widgetId, dependency) {
  return {
    ...widgetsDependencies,
    [widgetId]: {
      widgetId,
      dependency
    }
  };
}

export function* resolveWidgetDependency(
  prevState,
  state,
  widgetsDependencies,
  actionWidgetId,
  isVisible
) {
  const dependenciesKeys = keys(widgetsDependencies);
  for (let i = 0; i < dependenciesKeys.length; i++) {
    const { dependency, widgetId } = widgetsDependencies[dependenciesKeys[i]];
    const widgetDependenciesKeys = keys(dependency);
    console.log('point');
    console.log(widgetDependenciesKeys);
  }
}

export function* resolveDependency() {}

export function* watchDependency() {
  let widgetsDependencies = {};
  const channel = yield actionChannel([REGISTER_DEPENDENCY, SET]);
  while (true) {
    const prevState = yield select();
    const action = yield take(channel);
    const { type, payload } = action;
    const { widgetId, options = {}, prefix, key, model } = payload;
    const { dependency, dependencyType, isVisible } = options;
    const state = yield select();
    switch (type) {
      case REGISTER_DEPENDENCY: {
        widgetsDependencies = yield call(
          registerWidgetDependency,
          widgetsDependencies,
          widgetId,
          dependency
        );
        yield fork(
          resolveWidgetDependency,
          prevState,
          state,
          widgetsDependencies,
          widgetId,
          isVisible
        );
        break;
      }

      default:
        break;
    }
  }
}

export const widgetDependencySagas = [fork(watchDependency)];
