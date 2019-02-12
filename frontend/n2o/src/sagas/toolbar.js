import { takeEvery, select, put, call, all } from 'redux-saga/effects';
import { every, filter, forOwn, get, has, isEmpty, values } from 'lodash';
import { SET } from '../constants/models';
import { getContainerButtons, toolbarSelector } from '../selectors/toolbar';
import { changeButtonDisabled, changeButtonVisiblity } from '../actions/toolbar';
import evalExpression from '../utils/evalExpression';
import { REGISTER } from '../constants/widgets';
import { REGISTER_BUTTON } from '../constants/toolbar';

/**
 * резолв кондишена кнопки
 * @param conditions
 * @param model
 * @returns {boolean}
 */

export const resolveConditions = (conditions = [], model) =>
  every(conditions, ({ expression, modelLink }) =>
    evalExpression(expression, get(model, modelLink, {}))
  );

export function* handleAction(action) {
  const buttons = yield select(getContainerButtons(action.payload.key || action.payload.widgetId));
  yield all(values(buttons || []).map(v => call(resolveButton, v)));
}

/**
 * Функция для мониторинга изменения видимости родителя списка
 * @param key
 * @param id
 */
export function* setParentVisibleIfAllChildChangeVisible({ key, id }) {
  const buttons = yield select(getContainerButtons(key));
  const currentBtn = get(buttons, id);

  if (has(currentBtn, 'parentId')) {
    const parentId = get(currentBtn, 'parentId');
    const currentBtnGroup = filter(buttons, ['parentId', parentId]);

    const isAllChildHidden = every(currentBtnGroup, ['visible', false]);
    const isAllChildVisible = every(currentBtnGroup, ['visible', true]);
    const isParentVisible = get(buttons, [parentId, 'visible'], false);

    if (isAllChildHidden && isParentVisible) {
      yield put(changeButtonVisiblity(key, parentId, false));
    }
    if (isAllChildVisible && !isParentVisible) {
      yield put(changeButtonVisiblity(key, parentId, true));
    }
  }
}

export function checkOnNeed(button, modelLink) {
  const { conditions } = button;
  if (!conditions || isEmpty(conditions)) return false;
  const { visible = [], enabled = [] } = conditions;
  const cycleLength = visible.length > enabled.length ? visible.length : enabled.length;
  for (let i = 0; i < cycleLength; i++) {
    if (
      (visible && visible[i] && visible[i].modelLink === modelLink) ||
      (enabled && enabled[i] && enabled[i].modelLink === modelLink)
    ) {
      return button;
    }
  }
  return false;
}

export function getDependencyButtons(toolbar, modelLink) {
  const buttons = [];
  forOwn(toolbar, value => {
    if (!isEmpty(value)) {
      forOwn(value, button => {
        const neededButton = checkOnNeed(button, modelLink);
        if (neededButton) {
          buttons.push(neededButton);
        }
      });
    }
  });
  return buttons;
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
      yield call(setParentVisibleIfAllChildChangeVisible, button);
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

export function* updateButtonsConditions(action) {
  const { payload } = action;
  const modelLink = `models.${payload.prefix}['${payload.key}']`;
  const toolbar = yield select(toolbarSelector);
  const buttons = yield getDependencyButtons(toolbar, modelLink);
  yield all(buttons.map(button => call(resolveButton, button)));
}

export const toolbarSagas = [
  takeEvery([SET], updateButtonsConditions),
  takeEvery([REGISTER, REGISTER_BUTTON], handleAction)
];
