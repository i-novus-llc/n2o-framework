import isObject from 'lodash/isObject';
import values from 'lodash/values';
import every from 'lodash/every';
import isEmpty from 'lodash/isEmpty';

import functions from './functions';
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
  const joinedArgs = args.join(',');
  const key = `${joinedArgs}|||${code}`;

  if (!fooCache[key]) {
    fooCache[key] = new Function(
      windowKeys,
      `return function (${joinedArgs}) { return (${code}) }`
    )();
  }

  return fooCache[key];
}

const windowKeys = Object.keys(window).filter(v => !v.includes('-'));
const fooCache = {};

function evalExpressionSingle(expression, context) {
  try {
    const contextFinal =
      isObject(context) && !Array.isArray(context) ? context : {};
    const vars = { ...functions, ...window._n2oEvalContext, ...contextFinal };
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

function evalExpressionMulti(expression, context) {
  const multiContext = values(context);

  if (isEmpty(multiContext)) {
    return false;
  }

  return every(multiContext, context =>
    evalExpressionSingle(expression, context)
  );
}

/**
 * Выполняет JS выражение
 * @param expression {String} - Выражение, которое нужно выполнить
 * @param context - {Object} - Аргемент вызова (будет обогощен либами, типа lodash, moment и пр.)
 * @param type - {Object} - настройка evalExpression (mode = multi || mode = single default)
 * @returns {*} - результат вычисления
 */
export default function evalExpression(
  expression,
  context,
  type = { mode: 'single' }
) {
  const { mode } = type;

  return mode === 'multi'
    ? evalExpressionMulti(expression, context)
    : evalExpressionSingle(expression, context);
}
