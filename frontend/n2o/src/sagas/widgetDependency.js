import { actionChannel, call, fork, put, select, take } from 'redux-saga/effects';
import { RESOLVE_DEPENDENCY } from '../constants/widgets';
import { getModelsByDependency } from '../selectors/models';
import { DEPENDENCY_TYPES } from '../core/dependencyTypes';
import propsResolver from '../utils/propsResolver';
import {
  dataRequestWidget,
  disableWidget,
  enableWidget,
  hideWidget,
  showWidget
} from '../actions/widgets';

export function* resolveWidgetDependencies(options) {
  const { widgetId, dependency, isVisible } = options;
  const objectKeys = Object.keys(dependency);
  for (let i = 0; i < objectKeys.length; i++) {
    yield modify({
      widgetId,
      dependency,
      dependencyType: objectKeys[i],
      isVisible
    });
  }
}

/**
 * Резолв зависимостей виджета
 * @returns {IterableIterator<*>}
 */
export function* modify(options) {
  try {
    // const prevState = yield select();
    const state = yield select();
    const { widgetId, dependencyType, dependency, isVisible } = options;
    const model = getModelsByDependency(dependency)(state);
    // const prevModel = getModelsByDependency(dependency)(prevState);
    switch (dependencyType) {
      case DEPENDENCY_TYPES.visible: {
        yield call(resolveVisibleDependency, model, widgetId);
        break;
      }
      case DEPENDENCY_TYPES.enabled: {
        yield call(resolveEnabledDependency, model, widgetId);
        break;
      }
      case DEPENDENCY_TYPES.fetch: {
        yield call(resolveFetchDependency, model, {}, widgetId, isVisible);
        break;
      }
      default:
        break;
    }
  } catch (e) {
    console.error(e);
  }
}

/**
 * Резолв видимости
 * @param model
 * @param widgetId
 * @returns {IterableIterator<PutEffect<Action> | *>}
 */
export function* resolveVisibleDependency(model, widgetId) {
  try {
    const reduceFunction = (isVisible, { model, config }) => {
      return isVisible && propsResolver('`' + config.condition + '`', model);
    };
    const visible = model.reduce(reduceFunction, true);
    if (visible) {
      yield put(showWidget(widgetId));
    } else {
      yield put(hideWidget(widgetId));
    }
  } catch (e) {
    console.error(e);
  }
}

/**
 * Резолв активности
 * @param model
 * @param widgetId
 * @returns {IterableIterator<PutEffect<Action> | *>}
 */
export function* resolveEnabledDependency(model, widgetId) {
  try {
    const reduceFunction = (isDisabled, { model, config }) =>
      isDisabled && propsResolver('`' + config.condition + '`', model);
    const enabled = model.reduce(reduceFunction, true);
    if (enabled) {
      yield put(enableWidget(widgetId));
    } else {
      yield put(disableWidget(widgetId));
    }
  } catch (e) {
    console.error(e);
  }
}

/**
 * Резолв fetch зависимости
 * @param model
 * @param prevModel
 * @param widgetId
 * @param isVisible
 * @returns {IterableIterator<PutEffect<Action> | *>}
 */
export function* resolveFetchDependency(model, prevModel, widgetId, isVisible) {
  try {
    yield put(dataRequestWidget(widgetId));
  } catch (e) {
    console.error(e);
  }
}

export const widgetDependencySaga = [fork(modify)];
