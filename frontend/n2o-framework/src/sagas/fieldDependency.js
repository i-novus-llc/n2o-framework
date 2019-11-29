import {
  call,
  put,
  select,
  take,
  throttle,
  fork,
  takeEvery,
  delay,
  cancel,
} from 'redux-saga/effects';
import { isEmpty, isUndefined, some, includes, get } from 'lodash';
import { actionTypes, change } from 'redux-form';
import evalExpression from '../utils/evalExpression';

import { makeFormByName } from '../selectors/formPlugin';
import { REGISTER_FIELD_EXTRA } from '../constants/formPlugin';
import {
  enableField,
  disableField,
  showField,
  hideField,
  setRequired,
  unsetRequired,
  setLoading,
} from '../actions/formPlugin';
import { FETCH_VALUE } from '../core/api';
import fetchSaga from './fetch';
import compileUrl from '../utils/compileUrl';

export function* fetchValue(form, field, { dataProvider, valueFieldId }) {
  try {
    yield delay(300);
    yield put(setLoading(form, field, true));
    const state = yield select();
    const url = compileUrl(dataProvider.url, dataProvider, state);
    const response = yield call(fetchSaga, FETCH_VALUE, { url });
    const model = get(response, 'list[0]', null);

    if (model) {
      yield put(
        change(form, field, {
          keepDirty: false,
          value: valueFieldId ? model[valueFieldId] : model,
        })
      );
    }
  } catch (e) {
    throw e;
  } finally {
    yield put(setLoading(form, field, false));
  }
}

export function* modify(values, formName, fieldName, type, options = {}) {
  let _evalResult;
  if (options.expression) {
    _evalResult = evalExpression(options.expression, values);
  }

  switch (type) {
    case 'enabled':
      yield _evalResult
        ? put(enableField(formName, fieldName))
        : put(disableField(formName, fieldName));
      break;
    case 'visible':
      yield _evalResult
        ? put(showField(formName, fieldName))
        : put(hideField(formName, fieldName));
      break;
    case 'setValue':
      yield !isUndefined(_evalResult) &&
        put(
          change(formName, fieldName, {
            keepDirty: false,
            value: _evalResult,
          })
        );
      break;
    case 'reset':
      yield !isUndefined(_evalResult) &&
        put(
          change(formName, fieldName, {
            keepDirty: false,
            value: null,
          })
        );
      break;
    case 'required':
      yield _evalResult
        ? put(setRequired(formName, fieldName))
        : put(unsetRequired(formName, fieldName));
      break;
    case 'fetchValue':
      const watcher = yield fork(fetchValue, formName, fieldName, options);
      const action = yield take(actionTypes.CHANGE);

      if (get(action, 'meta.field') !== fieldName) {
        yield cancel(watcher);
      }
      break;
    default:
      break;
  }
}

export function* checkAndModify(
  values,
  fields,
  formName,
  fieldName,
  actionType
) {
  for (const entry of Object.entries(fields)) {
    const [fieldId, field] = entry;
    if (field.dependency) {
      for (const dep of field.dependency) {
        if (
          (fieldName && includes(dep.on, fieldName)) ||
          (fieldName &&
            some(
              dep.on,
              field => includes(field, '.') && includes(field, fieldName)
            )) ||
          ((actionType === actionTypes.INITIALIZE ||
            actionType === REGISTER_FIELD_EXTRA) &&
            dep.applyOnInit)
        ) {
          yield call(modify, values, formName, fieldId, dep.type, dep);
        }
      }
    }
  }
}

export function* resolveDependency(action) {
  try {
    const { form: formName, field: fieldName } = action.meta;
    const form = yield select(makeFormByName(formName));
    if (!isEmpty(form)) {
      const { values, registeredFields: fields } = form;
      if (!isEmpty(fields)) {
        yield call(
          checkAndModify,
          values,
          fields,
          formName,
          fieldName || action.name,
          action.type
        );
      }
    }
  } catch (e) {
    // todo: падает тут из-за отсуствия формы
    console.error(e);
  }
}

export function* catchAction(dispatch) {
  yield takeEvery(actionTypes.INITIALIZE, resolveDependency);
  yield throttle(300, REGISTER_FIELD_EXTRA, resolveDependency);
  yield takeEvery(actionTypes.CHANGE, resolveDependency);
}

export const fieldDependencySagas = [catchAction];
