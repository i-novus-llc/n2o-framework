import isObject from 'lodash/isObject'
import isFunction from 'lodash/isFunction'
import isArray from 'lodash/isArray'
import each from 'lodash/each'
import isString from 'lodash/isString'
import merge from 'lodash/merge'

import evalExpression, { parseExpression } from './evalExpression'

const blackList = [
    'dataProvider',
    'action',
    'actions',
    'queryMapping',
    'pathMapping',
]

export function resolve(code) {
    return new Function(
        'data',
        [
            'try{ with(Object.assign({}, data)){',
            `return ${code}`,
            '}}catch{ return false }',
        ].join('\n'),
    )
}

/**
 * Функция преобразует шаблоные props свойства вида \`name\` в константные данные из контекста
 * @param {Object} props - объект свойств которые требуется преобразовать
 * @param {Object} data - объект контекста, над которым будет произведенно преобразование
 * @param {Array} additionalBlackList - дополнительные исключения
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
export default function propsResolver(
    props,
    data = {},
    additionalBlackList = [],
) {
    let obj = {}
    if (isArray(props)) {
        obj = []
    }
    if (isObject(props) && !isFunction(props)) {
        for (const k in props) {
            if (isObject(props[k])) {
                if (merge(blackList, additionalBlackList).includes(k)) {
                    obj[k] = props[k]
                } else {
                    obj[k] = propsResolver(props[k], data)
                }
            } else if (parseExpression(props[k])) {
                obj[k] = evalExpression(parseExpression(props[k]), data)
            } else {
                obj[k] = props[k]
            }
        }
        each(props, (p, k) => {
            if (isObject(p)) {
                if (merge(blackList, additionalBlackList).includes(k)) {
                    obj[k] = p
                } else {
                    obj[k] = propsResolver(p, data)
                }
            } else if (parseExpression(p)) {
                obj[k] = evalExpression(parseExpression(p), data)
            } else {
                obj[k] = p
            }
        })
        return obj
    } if (isString(props)) {
        if (parseExpression(props)) {
            return evalExpression(parseExpression(props), data)
        }
        return props
    }
    return props
}
