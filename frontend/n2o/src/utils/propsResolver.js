import { isObject, isFunction, isArray, each, isString, isEmpty } from 'lodash';
import evalExpression, { parseExpression } from './evalExpression';
import functions from './functions';

const blackList = ['dataProvider', 'action'];

export function resolve(code) {
  return new Function(
    'data',
    [
      'try{ with(Object.assign({}, data)){',
      'return ' + code,
      '}}catch{ return false }',
    ].join('\n')
  );
}

function resolveString(str, data) {
  try {
    return resolve(str.slice(1, -1))(Object.assign({}, functions, data));
  } catch (e) {
    console.warn(
      `Ошибка при парсинге propsResolver! ${
        e.message
      }. Проверьте modelLink и текст`
    );
  }
}

/**
 * Функция преобразует шаблоные props свойства вида \`name\` в константные данные из контекста
 * @param {Object} props - объект свойств которые требуется преобразовать
 * @param {Object} data - объект контекста, над которым будет произведенно преобразование
 * @return {Object}
 * @example
 * const props = {
 *  fio: "`surname+' '+name+' '+middleName`"
 * }
 *
 * const model = {
 *  surname: "Иванов",
 *  name: "Иван",
 *  middleName: "Иванович",
 * }
 *
 * console.log(propsResolver(props, model))
 *
 * //- {fio: "Иванов Иван Иванович"}
 */
export default function propsResolver(props, data = {}) {
  let obj = {};
  if (isArray(props)) {
    obj = [];
  }
  if (isObject(props) && !isFunction(props)) {
    for (let k in props) {
      if (isObject(props[k])) {
        if (blackList.includes(k)) {
          obj[k] = props[k];
        } else {
          obj[k] = propsResolver(props[k], data);
        }
      } else if (parseExpression(props[k])) {
        obj[k] = evalExpression(parseExpression(props[k]), data);
      } else {
        obj[k] = props[k];
      }
    }
    each(props, (p, k) => {
      if (isObject(p)) {
        if (blackList.includes(k)) {
          obj[k] = p;
        } else {
          obj[k] = propsResolver(p, data);
        }
      } else if (parseExpression(p)) {
        obj[k] = evalExpression(parseExpression(p), data);
      } else {
        obj[k] = p;
      }
    });
    return obj;
  } else if (isString(props)) {
    if (parseExpression(props)) {
      return evalExpression(parseExpression(props), data);
    } else {
      return props;
    }
  }
  return props;
}
