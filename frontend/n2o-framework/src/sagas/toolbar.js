import {
  select,
  put,
  call,
  all,
  take,
  fork,
  takeLatest,
  cancelled,
} from 'redux-saga/effects';
import every from 'lodash/every';
import filter from 'lodash/filter';
import forOwn from 'lodash/forOwn';
import get from 'lodash/get';
import some from 'lodash/some';
import isEmpty from 'lodash/isEmpty';
import values from 'lodash/values';
import { SET } from '../constants/models';
import { getContainerButtons } from '../selectors/toolbar';
import {
  changeButtonDisabled,
  changeButtonVisiblity,
} from '../actions/toolbar';
import evalExpression from '../utils/evalExpression';
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
  const buttons = yield select(
    getContainerButtons(action.payload.key || action.payload.widgetId)
  );
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
  const parentId = get(currentBtn, 'parentId');

  if (parentId) {
    const currentBtnGroup = filter(buttons, ['parentId', parentId]);

    const isAllChildHidden = every(currentBtnGroup, ['visible', false]);
    const isAllChildVisible = some(currentBtnGroup, ['visible', true]);
    const isParentVisible = get(buttons, [parentId, 'visible'], false);

    if (isAllChildHidden && isParentVisible) {
      yield put(changeButtonVisiblity(key, parentId, false));
    }
    if (isAllChildVisible && !isParentVisible) {
      yield put(changeButtonVisiblity(key, parentId, true));
    }
  }
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

export function* watchModel(buttons, { payload }) {
  const { prefix, key } = payload;
  const modelLink = `models.${prefix}['${key}']`;

  if (buttons[modelLink]) {
    yield all(buttons[modelLink].map(button => fork(resolveButton, button)));
  }
}

function* watchRegister() {
  try {
    while (true) {
      const { payload: payloadRegister } = yield take(REGISTER_BUTTON);
      const { conditions } = payloadRegister;
      let buttons = {};

      if (conditions && !isEmpty(conditions)) {
        buttons = yield call(prepareButton, buttons, payloadRegister);
        yield fork(resolveButton, payloadRegister);
        // todo: Перейти на redux-saga@1.0.0 и использовать takeLeading
        yield takeLatest(SET, watchModel, buttons);
      }
    }
  } finally {
    if (yield cancelled()) {
      // todo: добавить cancel саги, когда кнопка unregister
    }
  }
}

export function prepareButton(buttons, payload) {
  const { conditions } = payload;
  let newButtons = Object.assign({}, buttons);
  let modelsLinkBuffer = [];
  forOwn(conditions, condition => {
    condition.map(({ modelLink }) => {
      if (!modelsLinkBuffer.includes(modelLink)) {
        const modelLinkArray = buttons[modelLink]
          ? [...buttons[modelLink], { ...payload }]
          : [{ ...payload }];
        newButtons = {
          ...newButtons,
          [modelLink]: modelLinkArray,
        };
        modelsLinkBuffer.push(modelLink);
      }
    });
  });

  return newButtons;
}

export const toolbarSagas = [fork(watchRegister)];
