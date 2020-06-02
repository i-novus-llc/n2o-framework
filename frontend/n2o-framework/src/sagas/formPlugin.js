import { takeEvery, put, select } from 'redux-saga/effects';
import { touch } from 'redux-form';
import values from 'lodash/values';
import get from 'lodash/get';
import set from 'lodash/set';

import { removeFieldMessage } from '../actions/formPlugin';
import { setModel } from '../actions/models';
import { COPY } from '../constants/models';
import {
  makeGetModelByPrefixSelector,
  modelsSelector,
} from '../selectors/models';
import evalExpression, { parseExpression } from '../utils/evalExpression';

export function* removeMessage(action) {
  yield action.meta &&
    action.meta.form &&
    action.meta.field &&
    put(removeFieldMessage(action.meta.form, action.meta.field));
}

export function* addTouched({ payload: { form, name } }) {
  yield put(touch(form, name));
}

export function* copyAction({ payload }) {
  const { target, source, mode = 'replace', sourceMapper } = payload;
  const state = yield select(modelsSelector);
  let sourceModel = get(state, values(source).join('.'));
  const targetModel = yield select(
    makeGetModelByPrefixSelector(target.prefix, target.key)
  );
  const expression = parseExpression(sourceMapper);
  let newModel = {};

  if (expression) {
    sourceModel = evalExpression(expression, sourceModel);
  }

  if (mode === 'merge') {
    newModel = target.field
      ? {
          ...targetModel,
          [target.field]: {
            ...targetModel[target.field],
            ...sourceModel,
          },
        }
      : { ...targetModel, ...sourceModel };
  } else if (mode === 'add') {
    if (!Array.isArray(sourceModel) || !Array.isArray(targetModel)) {
      throw new Error('Source or target is not an array!');
    }

    newModel = target.field
      ? {
          ...targetModel,
          [target.field]: [...targetModel, ...sourceModel],
        }
      : [...targetModel, ...sourceModel];
  } else {
    newModel = target.field
      ? {
          ...targetModel,
          [target.field]: sourceModel,
        }
      : sourceModel;
  }

  yield put(setModel(target.prefix, target.key, newModel));
}

export const formPluginSagas = [
  takeEvery('@@redux-form/START_ASYNC_VALIDATION', removeMessage),
  takeEvery(action => action.meta && action.meta.isTouched, addTouched),
  takeEvery(COPY, copyAction),
];
