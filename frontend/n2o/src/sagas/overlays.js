import { takeEvery, select, put, call, race } from 'redux-saga/effects';
import { isDirty } from 'redux-form';
import { CLOSE } from '../constants/overlays';
import { keys } from 'lodash';
import { makePageWidgetsByIdSelector } from '../selectors/pages';
import { showPrompt, destroyOverlay } from '../actions/overlays';

/**
 * Проверка на изменение данных в формах
 * @param name
 * @returns {IterableIterator<*>}
 */
export function* checkOnDirtyForm(name) {
  let someOneDirtyForm = false;
  const state = yield select();
  const widgets = makePageWidgetsByIdSelector(name)(state);
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
    yield put(destroyOverlay());
  } else {
    yield put(showPrompt(name));
  }
}

export const overlaysSagas = [
  takeEvery(CLOSE, checkPrompt),
  takeEvery(
    action =>
      action.meta &&
      action.payload &&
      !action.payload.prompt &&
      action.meta.closeLastModal,
    checkPrompt
  ),
];
