import { select, takeEvery, put, call, all } from 'redux-saga/effects';
import { SET } from '../constants/models';
import { REGISTER } from '../constants/widgets';
import evalExpression from '../utils/evalExpression';
import { getContainerButtons, toolbarSelector } from '../selectors/toolbar';
import { changeButtonDisabled, changeButtonVisiblity } from '../actions/toolbar';
import { get, every, values } from 'lodash';

/**
 * резолв кондишена кнопки
 * @param conditions
 * @param modelLink
 * @param model
 * @returns {boolean}
 */

export const resolveConditions = (conditions = [], model) =>
  every(conditions, ({ expression, modelLink }) =>
    evalExpression(expression, get(model, modelLink, {}))
  );

/**
 * резолв кондишена кнопки
 * @param conditions
 * @param modelLink
 * @param model
 * @returns {boolean}
 */

export function* handleAction(action) {
  const buttons = yield select(getContainerButtons(action.payload.key || action.payload.widgetId));
  yield all(values(buttons || []).map(v => call(resolveButton, v)));
}

/**
 * резолв всех условий
 * @param button
 * @param model
 */

export function* resolveButton(button) {
  const model = yield select();
  if (button.conditions) {
    const { visible, enabled } = button.conditions;
    if (visible) {
      const nextVisible = resolveConditions(visible, model);
      yield put(changeButtonVisiblity(button.key, button.id, nextVisible));
    }
    if (enabled) {
      const nextEnable = resolveConditions(enabled, model);
      yield put(changeButtonDisabled(button.key, button.id, !nextEnable));
    }
  }
  if (button.resolveEnabled) {
    const { modelLink: btnModelLink, on } = button.resolveEnabled;
    const modelOnLink = get(model, btnModelLink, {});
    const nextEnabled = on.some(o => modelOnLink[o]);
    yield put(changeButtonDisabled(button.key, button.id, !nextEnabled));
  }
}

export const modelSagas = [takeEvery([SET, REGISTER], handleAction)];
