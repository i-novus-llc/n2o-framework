import { takeEvery, put } from 'redux-saga/effects';
import { touch } from 'redux-form';
import { removeFieldMessage } from '../actions/formPlugin';
import { ADD_FIELD_MESSAGE } from '../constants/formPlugin';

export function* removeMessage(action) {
  yield action.meta &&
    action.meta.form &&
    action.meta.field &&
    put(removeFieldMessage(action.meta.form, action.meta.field));
}

export function* addTouched({ payload: { form, name } }) {
  yield put(touch(form, name));
}

export const formPluginSagas = [
  takeEvery('@@redux-form/START_ASYNC_VALIDATION', removeMessage),
  takeEvery(ADD_FIELD_MESSAGE, addTouched)
];
