import { takeEvery, select, put, call } from 'redux-saga/effects';
import { isDirty } from 'redux-form';
import { CLOSE } from '../constants/modals';
import { keys } from 'lodash';
import { makePageMetadataByIdSelector } from '../selectors/pages';
import { showPrompt, destroyModal } from '../actions/modals';

/**
 * Проверка на изменение данных в формах
 * @param name
 * @returns {IterableIterator<*>}
 */
export function* checkOnDirtyForm(name) {
  let someOneDirtyForm = false;
  const state = yield select();
  const widgets = makePageMetadataByIdSelector(name)(state).widgets;
  const widgetsKeys = keys(widgets);
  for (let i = 0; i < widgetsKeys.length; i++) {
    if (widgets[widgetsKeys[i]].src === 'FormWidget') {
      someOneDirtyForm = isDirty(widgetsKeys[i])(state);
    }
  }
  return someOneDirtyForm;
}

/**
 * Функция показа подтверждения закрытия
 * @param action
 * @returns {IterableIterator<*>}
 */
export function* checkPrompt(action) {
  const { name, prompt } = action.payload;
  let needToShowPrompt = false;
  if (prompt) {
    needToShowPrompt = yield call(checkOnDirtyForm, name);
  }
  if (!needToShowPrompt) {
    yield put(destroyModal());
  } else {
    yield put(showPrompt(name));
  }
}

export const modalsSagas = [
  takeEvery(CLOSE, checkPrompt),
  takeEvery(action => action.meta && action.meta.closeLastModal, checkPrompt)
];
