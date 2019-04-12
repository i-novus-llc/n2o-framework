import { call, put, select, takeEvery, throttle } from 'redux-saga/effects';
import { isEmpty, isEqual, get } from 'lodash';
import { actionTypes } from 'redux-form';
import evalExpression from '../utils/evalExpression';
import { updateModel } from '../actions/models';
import { makeFormByName } from '../selectors/formPlugin';
import { REGISTER_FIELD_EXTRA } from '../constants/formPlugin';
import {
  enableField,
  disableField,
  showField,
  hideField,
  setRequired,
  unsetRequired,
} from '../actions/formPlugin';
import { PREFIXES } from '../constants/models';
import { makeGetResolveModelSelector } from '../selectors/models';

export function* modify(values, formName, fieldName, type, options = {}) {
  let _evalResult;
  if (options.expression) {
    _evalResult = evalExpression(options.expression, values);
  }
  const prevFieldValue = yield select(makeGetResolveModelSelector(formName));
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
      if (!isEqual(get(prevFieldValue, fieldName, undefined), _evalResult)) {
        yield put(
          updateModel(PREFIXES.resolve, formName, fieldName, _evalResult)
        );
      }
      break;
    case 'reset':
      yield _evalResult &&
        put(updateModel(PREFIXES.resolve, formName, fieldName, null));
      break;
    case 'required':
      yield _evalResult
        ? put(setRequired(formName, fieldName))
        : put(unsetRequired(formName, fieldName));
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
          (fieldName && dep.on.includes(fieldName)) ||
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
