import { select, fork, takeEvery, actionChannel, take, call } from 'redux-saga/effects';
import { REGISTER_DEPENDENCY } from '../constants/dependency';
import { SET } from '../constants/models';

export function* registerDependency(widgetsDependencies, widgetId, { dependency }) {
  return {
    ...widgetsDependencies,
    [widgetId]: dependency
  };
}

export function* resolveDependency(dependency, prevState, state) {
  console.log('point');
  console.log(arguments);
  // TODO
}

export function* watchDependency() {
  console.log('point');
  let widgetsDependencies = {};
  const channels = yield actionChannel([REGISTER_DEPENDENCY, SET]);
  while (true) {
    const prevState = yield select();
    const action = yield take(channels);
    const state = yield select();
    const { type, payload } = action;
    const { prefix, key, model, widgetId, options } = payload;
    switch (type) {
      case REGISTER_DEPENDENCY: {
        widgetsDependencies = yield call(
          registerDependency,
          widgetsDependencies,
          widgetId,
          options
        );
        yield resolveDependency(widgetsDependencies[widgetId], prevState, state);
        break;
      }
      case SET: {
        yield resolveDependency(widgetsDependencies[key], prevState, state);
        break;
      }
      default:
        break;
    }
  }
}

export const dependencies = [fork(watchDependency)];
