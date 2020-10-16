import isObject from 'lodash/isObject';

import warning from './warning';

/**
 * Проверяет, является ли строка JS выражением
 * @param value {String} - Проверяемая строка
 * @returns {String|Boolean} - Найденное JS выражение, или false
 */
export function parseExpression(value) {
  const res = String(value).match('^`(.*)`$');

  if (res && res[1]) {
    return res[1];
  }

  return false;
}

/**
 * Создает функцию из текста
 * @param args {String[]} - массив имен переменных
 * @param code {String} - код для выполнения
 * @returns {Function} - Функция, созданная из текста code
 */
export function createContextFn(args, code) {
  return new Function(args.join(','), `return ${code}`);
}

/**
 * Выполняет JS выражение
 * @param expression {String} - Выражение, которо нужно выполнить
 * @param context - {Object} - Аргемент вызова (бедет обогощен либами, типа lodash, moment и пр.)
 * @returns {*}
 */
export default function evalExpression(expression, context) {
  try {
    const contextFinal = isObject(context) ? context : {};
    const vars = { ...window._n2oEvalContext, ...contextFinal };
    const fn = createContextFn(Object.keys(vars), expression);

    return fn.apply(context || {}, Object.values(vars));
  } catch (e) {
    warning(
      e,
      `Ошибка при выполнение evalExpression! ${e.message}.
      \nВыражение: ${expression}
      \nКонтекст: ${JSON.stringify(context)}`
    );
  }
}
