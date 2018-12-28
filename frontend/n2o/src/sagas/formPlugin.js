import { takeEvery, put } from 'redux-saga/effects';

import { removeFieldMessage } from '../actions/formPlugin';

function* removeMessage(action) {
  yield action.meta &&
    action.meta.form &&
    action.meta.field &&
    put(removeFieldMessage(action.meta.form, action.meta.field));
}

export const formPluginSagas = [takeEvery('@@redux-form/START_ASYNC_VALIDATION', removeMessage)];
