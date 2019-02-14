import { runSaga } from 'redux-saga';
import {
  isEmpty,
  toString,
  isNumber,
  isUndefined,
  isNull,
  isNaN,
  isString,
  isObject,
  get
} from 'lodash';
import evalExpression from '../../utils/evalExpression';
import fetchSaga from '../../sagas/fetch.js';
import { FETCH_VALIDATE } from '../api.js';

/**
 * Валидация того, что email
 * @param fieldId
 * @param values
 * @returns {boolean}
 */
export function email(fieldId, values) {
  return (
    isString(values[fieldId]) && /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values[fieldId])
  );
}

/**
 * Валидация того, что поле непустое
 * @param fieldId
 * @param values
 * @param options
 * @returns {*}
 */
export function required(fieldId, values, options = {}) {
  const value = get(values, fieldId);
  if (options.expression && !evalExpression(options.expression, values)) {
    return true;
  }
  if (isString(value)) {
    return value !== '';
  } else if (isObject(value)) {
    return !isEmpty(value);
  } else {
    return !isUndefined(values[fieldId]) && !isNull(values[fieldId]) && !isNaN(values[fieldId]);
  }
}

/**
 * Валидация js-condition
 * @param fieldId
 * @param values
 * @param options
 * @returns {boolean}
 */
export function condition(fieldId, values, options) {
  return evalExpression(options.expression, values);
}

/**
 * Валидация с отправкой запроса на сервер
 * @param fieldId
 * @param values
 * @param options
 * @param dispatch
 * @returns {boolean|*}
 */
export async function constraint(fieldId, values, options, dispatch) {
  if (!isEmpty(values[fieldId])) {
    return await runSaga(
      {
        dispatch
      },
      fetchSaga,
      FETCH_VALIDATE,
      { validationKey: options.validationKey }
    ).done;
  } else {
    return Promise.reject();
  }
}

/**
 * Валидация того, что integer
 * @param fieldId
 * @param values
 * @returns {boolean}
 */
export function integer(fieldId, values) {
  const v = values[fieldId];
  return v && !isNaN(Number(v)) && isNumber(Number(v));
}

/**
 * Валидация минимальной длины
 * @param fieldId
 * @param values
 * @param options
 * @returns {boolean}
 */
export function minLength(fieldId, values, options) {
  return isString(values[fieldId]) && values[fieldId].length > options.min;
}

/**
 * Валиадация максимальной длины
 * @param fieldId
 * @param values
 * @param options
 * @returns {boolean}
 */
export function maxLength(fieldId, values, options) {
  return isString(values[fieldId]) && values[fieldId].length < options.max;
}

/**
 * Соответствие поля значению из метаданных
 * @param fieldId
 * @param values
 * @param options
 * @returns {boolean}
 */
export function match(fieldId, values, options) {
  return values[fieldId] && toString(values[fieldId]) === toString(options.field);
}
