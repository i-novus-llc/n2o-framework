import isObject from 'lodash/isObject';
import warning from './warning';
import functions from './functions';
const windowObjectHack = {};
Object.keys(window)
  .filter(v => !v.includes('-'))
  .forEach(wh => (windowObjectHack[wh] = undefined));

/**
 * Проверяет и возвращает значение, если это js выражение
 * @param value
 * @returns {*}
 */
export function parseExpression(value) {
  value = String(value);
  const res = value.match('^`(.*)`$');
  if (res && res[1]) {
    return res[1];
  }
  return false;
}

/**
 * Создает функцию с контекстом
 * @param vars - массив переменных
 * @param code - код для выполнения
 * @returns {*}
 */
export function createContextFn(vars, code) {
  return new Function(vars.join(','), ['return ' + code].join('\n'));
}

/**
 * Выполняет JS выражение с контекстом
 * @param expression
 * @param context
 * @returns {*}
 */
export default function evalExpression(expression, context) {
  try {
    const vars = Object.assign(
      {},
      windowObjectHack,
      functions,
      isObject(context) ? context : {}
    );
    const fn = createContextFn(Object.keys(vars), expression);
    return fn.apply(context || {}, Object.values(vars));
  } catch (e) {
    warning(
      e,
      `Ошибка при выполнение evalExpression! ${
        e.message
      }.\nВыражение: ${expression}\nКонтекст: ${JSON.stringify(context)}`
    );
  }
}
