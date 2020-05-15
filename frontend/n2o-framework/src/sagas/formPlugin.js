import { takeEvery, put, select } from 'redux-saga/effects';
import { touch } from 'redux-form';
import get from 'lodash/get';
import isEmpty from 'lodash/isEmpty';

import { removeFieldMessage } from '../actions/formPlugin';
import { makeFieldByName } from '../selectors/formPlugin';

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

export const formPluginSagas = [
  takeEvery('@@redux-form/START_ASYNC_VALIDATION', removeMessage),
  takeEvery(action => action.meta && action.meta.isTouched, addTouched),
];
