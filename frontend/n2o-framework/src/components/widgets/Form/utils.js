import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import map from 'lodash/map'
import reduce from 'lodash/reduce'
import set from 'lodash/set'
import merge from 'lodash/merge'
import has from 'lodash/has'
import each from 'lodash/each'
import isObjectLike from 'lodash/isObjectLike'
import isNil from 'lodash/isNil'
import isBoolean from 'lodash/isBoolean'
import isEmpty from 'lodash/isEmpty'

import evalExpression, { parseExpression } from '../../../utils/evalExpression'

/**
 * Возвращает id первового поля, на котором может быть установлен автофокус
 * @param fields
 * @return {*}
 */
// eslint-disable-next-line consistent-return
export function getAutoFocusId(fields) {
    // eslint-disable-next-line no-restricted-syntax
    for (const field of fields) {
        if (!field.readOnly && field.visible !== false && field.enabled !== false) {
            return field.id
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
    fields = []
    if (isObjectLike(obj)) {
        each(obj, (v, k) => {
            if (k === 'fields') {
                fields = fields.concat(obj.fields)
            } else {
                fields = fields.concat(flatFields(obj[k], fields))
            }
        })
    }

    return fields
}

/**
 * Запрашивает данные, если зависимое значение было изменено
 * @param prevState
 * @param state
 * @param ref
 */
export function fetchIfChangeDependencyValue(prevState, state, ref) {
    // eslint-disable-next-line no-underscore-dangle
    if (!isEqual(prevState, state) && ref && ref.props._fetchData) {
        const { _fetchData, size, labelFieldId } = ref.props

        _fetchData({
            size,
            [`sorting.${labelFieldId}`]: 'ASC',
        })
    }
}

export const getFieldsKeys = (fieldsets) => {
    const keys = []

    const mapFields = (fields, name) => {
        map(fields, ({ id }) => keys.push(name ? `${name}[].${id}` : id))
    }

    const mapCols = (cols, name) => {
        map(cols, (col) => {
            if (has(col, 'cols')) {
                mapCols(col.cols, name)
            } else if (has(col, 'fields')) {
                mapFields(col.fields, name)
            } else if (has(col, 'fieldsets')) {
                keys.push(...getFieldsKeys(col.fieldsets))
            }
        })
    }

    map(fieldsets, ({ rows, name = null }) => map(rows, (row) => {
        mapCols(row.cols, name)
    }))

    return keys
}

const pickByPath = (object, arrayToPath) => reduce(
    arrayToPath,
    // eslint-disable-next-line consistent-return
    (o, p) => {
        if (has(object, p)) {
            return set(o, p, get(object, p))
        }
    },
    {},
)

export const setWatchDependency = (state, props, dependencyType) => {
    const { dependency, form, modelPrefix } = props

    const pickByReRender = (acc, { type, on }) => {
        if (on && type === dependencyType) {
            const formOn = map(on, item => ['models', modelPrefix, form, item].join('.'))

            return merge(acc, pickByPath(state, formOn))
        }

        return acc
    }

    return reduce(dependency, pickByReRender, {})
}

/**
 * @param {string|boolean} [value]
 * @param {object} activeModel
 * @return {boolean}
 */
export const resolveExpression = (value, activeModel) => {
    if (isNil(value)) {
        return true
    }
    if (isBoolean(value)) {
        return value
    }
    if (isEmpty(activeModel)) {
        return false
    }

    return evalExpression(parseExpression(value), activeModel)
}
