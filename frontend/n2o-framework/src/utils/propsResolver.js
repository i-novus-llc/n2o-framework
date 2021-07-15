import isObject from 'lodash/isObject'
import isFunction from 'lodash/isFunction'
import isArray from 'lodash/isArray'
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
    // eslint-disable-next-line no-new-func
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
    const obj = isArray(props) ? [] : {}
    const fullBlackList = merge(blackList, additionalBlackList)

    if (isObject(props) && !isFunction(props)) {
        Object.keys(props).forEach((key) => {
            const property = props[key]

            if (isObject(property)) {
                obj[key] = fullBlackList.includes(key)
                    ? property : propsResolver(property, data)
            } else {
                const parsedExpression = parseExpression(property)

                obj[key] = parsedExpression ? evalExpression(parsedExpression, data) : property
            }
        })

        return obj
    }

    if (isString(props)) {
        const parsedExpression = parseExpression(props)

        if (parsedExpression) {
            return evalExpression(parsedExpression, data)
        }
    }

    return props
}
