import get from 'lodash/get';
import isNumber from 'lodash/isNumber';
import isUndefined from 'lodash/isUndefined';
import isNil from 'lodash/isNil';
import isBoolean from 'lodash/isBoolean';
import evalExpression, { parseExpression } from './evalExpression';

/**
 * Получение значения по ссылке и выражению.
 * @param state
 * @param link
 * @param value
 * @returns {*}
 */
export default function linkResolver(state, { link, value }) {
  if (!link && isNil(value)) return;

  if (isBoolean(value)) return value;
  if (isNumber(value)) return value;
  const context = get(state, link);
  if (isUndefined(value) && link) return context;

  const json = JSON.stringify(value);
  const str = JSON.parse(json, (k, val) => {
    const parsedValue = parseExpression(val);
    return parsedValue ? evalExpression(parsedValue, context) : val;
  });
  return str;
}
