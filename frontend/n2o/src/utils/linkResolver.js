import { get, isNumber, isString } from 'lodash';
import evalExpression, { parseExpression } from './evalExpression';

function parseAndReplace(string, context) {
  return string.replace(/\"\`([^`]*)\`\"/gim, (_, expression) => {
    const res = evalExpression(expression, context);
    return isString(res) ? `"${res}"` : res;
  });
}

/**
 * Получение значения по ссылке и выражению.
 * @param state
 * @param link
 * @param value
 * @returns {*}
 */
export default function linkResolver(state, { link, value }) {
  if (!link && !value) return;

  if (isNumber(value)) return value;
  const context = get(state, link);
  if (!value && link) return context;

  if (isString(value)) {
    const parsedValue = parseExpression(value);
    return parsedValue ? evalExpression(parsedValue, context) : value;
  }

  const json = JSON.stringify(value);
  const resultString = parseAndReplace(json, context);
  return JSON.parse(resultString);
}
