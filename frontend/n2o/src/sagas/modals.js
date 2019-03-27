import { takeEvery, select, put } from 'redux-saga/effects';
import { isDirty } from 'redux-form';
import { CLOSE, DESTROY } from '../constants/modals';
import { keys } from 'lodash';
import { makePageMetadataByIdSelector } from '../selectors/pages';
import { showPrompt, hidePrompt, destroyModal } from '../actions/modals';

export function* hideModalPrompt(name) {
  yield put(hidePrompt(name));
}

export function* checkPrompt(action) {
  const { name, prompt } = action.payload;
  let someOneDirtyForm = false;
  if (prompt) {
    const state = yield select();
    const widgets = makePageMetadataByIdSelector(name)(state).widgets;
    const widgetsKeys = keys(widgets);
    for (let i = 0; i < widgetsKeys.length; i++) {
      if (widgets[widgetsKeys[i]].src === 'FormWidget') {
        someOneDirtyForm = isDirty(widgetsKeys[i])(state);
      }
    }
    if (someOneDirtyForm) {
      yield put(showPrompt(name));
    }
  }
  if (!prompt || !someOneDirtyForm) {
    yield put(destroyModal());
  }
}

export const modalsSagas = [takeEvery(CLOSE, checkPrompt)];
