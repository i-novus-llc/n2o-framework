import { takeEvery, select, put, call, all } from 'redux-saga/effects';
import { forOwn, isEmpty } from 'lodash';
import { SET } from '../constants/models';
import { toolbarSelector } from '../selectors/toolbar';
import { changeButtonDisabled, changeButtonVisiblity } from '../actions/toolbar';
import { resolveConditions, setParentVisibleIfAllChildChangeVisible } from './model';

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

export function* updateButton(button) {
  const model = yield select();
  const { conditions } = button;
  if (conditions) {
    const { visible, enabled } = conditions;
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
}

export function* updateButtonsConditions(action) {
  const { payload } = action;
  const modelLink = `models.${payload.prefix}['${payload.key}']`;
  const toolbar = yield select(toolbarSelector);
  const buttons = yield getDependencyButtons(toolbar, modelLink);
  yield all(buttons.map(button => call(updateButton, button)));
}

export const toolbarSagas = [takeEvery(SET, updateButtonsConditions)];
