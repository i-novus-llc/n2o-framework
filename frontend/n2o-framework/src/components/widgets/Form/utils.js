import map from 'lodash/map'
import has from 'lodash/has'
import each from 'lodash/each'
import isObjectLike from 'lodash/isObjectLike'
import isNil from 'lodash/isNil'

import { propsResolver } from '../../../core/Expression/propsResolver'

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

export const getFieldsKeys = (fieldsets) => {
    /** @type {string[]} */
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

/**
 * @param {string|boolean} [value]
 * @param {object} model
 * @return {boolean}
 */
export const resolveExpression = (value, model, context) => {
    if (isNil(value)) {
        return true
    }

    return propsResolver(value, model, context)
}
