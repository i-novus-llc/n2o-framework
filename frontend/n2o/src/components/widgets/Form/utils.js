import _, { isEqual, get, map, reduce, set } from 'lodash';

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

/**
 * Запрашивает данные, если зависимое значение было изменено
 * @param prevState
 * @param state
 * @param ref
 */
export function fetchIfChangeDependencyValue(prevState, state, ref) {
  if (!isEqual(prevState, state) && ref && ref.props._fetchData) {
    const { _fetchData, size, labelFieldId } = ref.props;
    _fetchData({
      size: size,
      [`sorting.${labelFieldId}`]: 'ASC'
    });
  }
}

const pickByPath = (object, arrayToPath) =>
  reduce(arrayToPath, (o, p) => set(o, p, get(object, p)), {});

const DEPENDENCY_TYPES = {
  RE_RENDER: 'reRender'
};

export const setWatchDependency = (state, props) => {
  const { dependency, form } = props;

  const pickByReRender = (acc, { type, on }) => {
    if (on && type === DEPENDENCY_TYPES.RE_RENDER) {
      const formOn = map(on, item => ['form', form, 'values', on].join('.'));
      return { ...acc, ...pickByPath(state, formOn) };
    }
  };
  return reduce(dependency, pickByReRender, {});
};
