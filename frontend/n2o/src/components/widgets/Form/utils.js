import _ from 'lodash';

/**
 * Возвращает id первового поля, на котором может быть установлен автофокус
 * @param fields
 * @return {*}
 */
export function getAutoFocusId(fields) {
  for (let field of fields) {
    if (!field.readOnly && field.visible !== false && field.enabled !== false) {
      return field.id;
    }
  }
}

/**
 *
 * Делает из сложного объекта с филдами разных уровнях плоский массив филдов (обходит объект рекурсивно)
 * @param obj - объект, откуда достаем филды
 * @param fields  - текущий массив филдов
 * @example
 * // вернет плоский массив филдов fieldset'а
 * flatFields(fieldset, [])
 */
export function flatFields(obj, fields) {
  fields = [];
  if (_.isObjectLike(obj)) {
    _.each(obj, (v, k) => {
      if (k === 'fields') {
        fields = fields.concat(obj.fields);
      } else {
        fields = fields.concat(flatFields(obj[k], fields));
      }
    });
  }
  return fields;
}
