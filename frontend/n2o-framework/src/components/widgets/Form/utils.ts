import map from 'lodash/map'
import has from 'lodash/has'
import each from 'lodash/each'
import isObjectLike from 'lodash/isObjectLike'

import { ColProps, type Fields, type FieldSetsProps, type FieldsetProps, RowProps, FieldType } from './types'

/**
 * Возвращает id первового поля, на котором может быть установлен автофокус
 * @param fields
 */
// eslint-disable-next-line consistent-return
export function getAutoFocusId(fields: Fields) {
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
export function flatFields(obj: FieldSetsProps | FieldsetProps | RowProps | ColProps, fields?: FieldType[]) {
    fields = []
    if (isObjectLike(obj)) {
        each(obj, (_v, k) => {
            if (k === 'fields') {
                fields = fields?.concat((obj as ColProps).fields)
            } else {
                // @ts-ignore FIXME необходим рефактоинг, сложная входная структура, одновременное обращение по индексу и ключу
                fields = fields.concat(flatFields(obj[k as 'fields' | number | 'cols' | 'rows'], fields))
            }
        })
    }

    return fields
}

export const getFieldsKeys = (fieldsets?: FieldSetsProps) => {
    const keys: string[] = []

    const mapFields = (fields: Fields, name?: string | null) => {
        map(fields, ({ id }) => keys.push(name ? `${name}[].${id}` : id))
    }

    const mapCols = (cols?: ColProps[], name?: string | null) => {
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
