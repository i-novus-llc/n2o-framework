import { get } from 'lodash';
import evalExpression, { parseExpression } from './evalExpression';

/**
 * Получение значения по ссылке и выражению.
 * @param state
 * @param link
 * @param value
 * @returns {*}
 */
export default function linkResolver(state, { link, value }) {
  const parsedValue = parseExpression(value);
  let linkValue;
  if (link) {
    linkValue = get(state, link);
  }

  if (parsedValue) {
    return evalExpression(parsedValue, linkValue);
  } else {
    return value ? value : linkValue;
  }
}
