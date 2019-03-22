import { select, fork, actionChannel, take, call, put } from 'redux-saga/effects';
import { isEmpty, keys, isEqual, reduce } from 'lodash';
import { REGISTER_DEPENDENCY } from '../constants/dependency';
import { SET } from '../constants/models';
import { DEPENDENCY_TYPES } from '../core/dependencyTypes';
import {
  dataRequestWidget,
  showWidget,
  hideWidget,
  enableWidget,
  disableWidget
} from '../actions/widgets';
import { getModelsByDependency } from '../selectors/models';
import { makeWidgetVisibleSelector } from '../selectors/widgets';
import propsResolver from '../utils/propsResolver';

export const reduceFunction = (isTrue, { model, config }) => {
  return isTrue && propsResolver('`' + config.condition + '`', model);
};

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

/**
 * Резолв всех зависимостей виджета
 * @param prevState
 * @param state
 * @param widgetsDependencies
 * @param actionWidgetId
 * @returns {IterableIterator<*|CallEffect>}
 */
export function* resolveWidgetDependency(prevState, state, widgetsDependencies, actionWidgetId) {
  const dependenciesKeys = keys(widgetsDependencies);
  for (let i = 0; i < dependenciesKeys.length; i++) {
    const { dependency, widgetId } = widgetsDependencies[dependenciesKeys[i]];
    const widgetDependenciesKeys = keys(dependency);
    const isVisible = makeWidgetVisibleSelector(widgetId)(state);
    for (let j = 0; j < widgetDependenciesKeys.length; j++) {
      const prevModel = getModelsByDependency(dependency[widgetDependenciesKeys[j]])(prevState);
      const model = getModelsByDependency(dependency[widgetDependenciesKeys[j]])(state);
      yield fork(
        resolveDependency,
        widgetDependenciesKeys[j],
        dependency[widgetDependenciesKeys[j]],
        widgetId,
        model,
        prevModel,
        isVisible
      );
    }
  }
}

/**
 * Резолв конкретной зависимости по типу
 * @param dependencyType
 * @param dependency
 * @param widgetId
 * @param model
 * @param prevModel
 * @param isVisible
 * @returns {IterableIterator<*|CallEffect>}
 */
export function* resolveDependency(
  dependencyType,
  dependency,
  widgetId,
  model,
  prevModel,
  isVisible
) {
  switch (dependencyType) {
    case DEPENDENCY_TYPES.fetch: {
      yield call(resolveFetchDependency, dependency, widgetId, model, prevModel, isVisible);
      break;
    }
    case DEPENDENCY_TYPES.visible: {
      yield call(resolveVisibleDependency, dependency, widgetId, model, prevModel);
      break;
    }
    case DEPENDENCY_TYPES.enabled: {
      yield call(resolveEnabledDependency, dependency, widgetId, model, prevModel);
      break;
    }
    default:
      break;
  }
}

/**
 * Резолв запросов
 * @param dependency
 * @param widgetId
 * @param model
 * @param prevModel
 * @param isVisible
 * @returns {IterableIterator<*>}
 */
export function* resolveFetchDependency(dependency, widgetId, model, prevModel, isVisible) {
  if (!isEqual(prevModel, model) && isVisible) {
    yield put(dataRequestWidget(widgetId));
  }
}

/**
 * Резолв видимости
 * @param dependency
 * @param widgetId
 * @param model
 * @param prevModel
 * @returns {IterableIterator<*>}
 */
export function* resolveVisibleDependency(dependency, widgetId, model, prevModel) {
  if (!isEqual(prevModel, model)) {
    const visible = reduce(model, reduceFunction, true);
    if (visible) {
      yield put(showWidget(widgetId));
    } else {
      yield put(hideWidget(widgetId));
    }
  }
}

/**
 * Резолв активности
 * @param dependency
 * @param widgetId
 * @param model
 * @param prevModel
 * @returns {IterableIterator<*>}
 */
export function* resolveEnabledDependency(dependency, widgetId, model, prevModel) {
  if (!isEqual(prevModel, model)) {
    const enabled = reduce(model, reduceFunction, true);
    if (enabled) {
      yield put(enableWidget(widgetId));
    } else {
      yield put(disableWidget(widgetId));
    }
  }
}

/**
 * Наблюдение за регистрацией зависимостей виджетов и изменением модели
 * @returns {IterableIterator<*>}
 */
export function* watchDependency() {
  let widgetsDependencies = {};
  const channel = yield actionChannel([REGISTER_DEPENDENCY, SET]);
  while (true) {
    const prevState = yield select();
    const action = yield take(channel);
    const { type, payload } = action;
    const { widgetId, options = {}, prefix, key, model } = payload;
    const { dependency, isVisible } = options;
    const state = yield select();
    switch (type) {
      case REGISTER_DEPENDENCY: {
        widgetsDependencies = yield call(
          registerWidgetDependency,
          widgetsDependencies,
          widgetId,
          dependency
        );
        yield fork(resolveWidgetDependency, prevState, state, widgetsDependencies, widgetId);
        break;
      }
      case SET: {
        yield fork(resolveWidgetDependency, prevState, state, widgetsDependencies, key);
        break;
      }
      default:
        break;
    }
  }
}

export const widgetDependencySagas = [fork(watchDependency)];
