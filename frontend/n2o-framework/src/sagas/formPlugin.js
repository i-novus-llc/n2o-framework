import { takeEvery, put, select } from 'redux-saga/effects';
import { touch, change } from 'redux-form';

import get from 'lodash/get';
import isEmpty from 'lodash/isEmpty';
import values from 'lodash/values';
import split from 'lodash/split';
import includes from 'lodash/includes';
import merge from 'lodash/merge';
import setWith from 'lodash/setWith';

import { removeFieldMessage } from '../actions/formPlugin';
import { makeFieldByName } from '../selectors/formPlugin';
import { setModel } from '../actions/models';
import { COPY } from '../constants/models';
import {
  makeGetModelByPrefixSelector,
  modelsSelector,
} from '../selectors/models';
import evalExpression, { parseExpression } from '../utils/evalExpression';

export function* removeMessage(action) {
  const formName = get(action, 'meta.form');
  const fieldName = get(action, 'meta.field');

  if (formName && fieldName) {
    const field = yield select(makeFieldByName(formName, fieldName));
    const fieldValidation = get(field, 'validation');

    if (!fieldValidation || isEmpty(fieldValidation)) {
      yield put(removeFieldMessage(action.meta.form, action.meta.field));
    }
  }
}

export function* addTouched({ payload: { form, name } }) {
  yield put(touch(form, name));
}

export function* copyAction({ payload }) {
  const { target, source, mode = 'replace', sourceMapper } = payload;
  const state = yield select(modelsSelector);
  let sourceModel = get(state, values(source).join('.'));
  let selectedTargetModel = yield select(
    makeGetModelByPrefixSelector(target.prefix, target.key)
  );
  let targetModel = selectedTargetModel || [];
  const expression = parseExpression(sourceMapper);
  let newModel = {};
  const targetModelField = get(targetModel, [target.field], []);

  const path = target.field;
  const treePath = includes(split(path, ''), '.');

  const withTreeObject = (path, sheetValue) => setWith({}, path, sheetValue);

  if (expression) {
    sourceModel = evalExpression(expression, sourceModel);
  }

  if (mode === 'merge') {
    newModel = target.field
      ? {
          ...targetModel,
          [target.field]: {
            ...targetModelField,
            ...sourceModel,
          },
        }
      : { ...targetModel, ...sourceModel };
  } else if (mode === 'add') {
    if (!Array.isArray(sourceModel) || !Array.isArray(targetModelField)) {
      console.warn('Source or target is not an array!');
    }

    sourceModel = Object.values(sourceModel);

    newModel = target.field
      ? {
          ...targetModel,
          [target.field]: [...targetModelField, ...sourceModel],
        }
      : [...targetModelField, ...sourceModel];
  } else {
    if (treePath) {
      newModel = target.field
        ? {
            ...merge({}, targetModel, withTreeObject(path, sourceModel)),
          }
        : sourceModel;
    } else {
      newModel = target.field
        ? {
            ...targetModel,
            [target.field]: sourceModel,
          }
        : sourceModel;
    }
  }
  yield put(change(target.key, target.field, newModel[path]));
  yield put(setModel(target.prefix, target.key, newModel));
}

export const formPluginSagas = [
  takeEvery('@@redux-form/START_ASYNC_VALIDATION', removeMessage),
  takeEvery(action => action.meta && action.meta.isTouched, addTouched),
  takeEvery(COPY, copyAction),
];
