import isObject from 'lodash/isObject'
import uniq from 'lodash/uniq'
import isEmpty from 'lodash/isEmpty'

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
    if (!props) { return props }
    if (typeof props === 'string') {
        const parsedExpression = parseExpression(props)

        if (parsedExpression) {
            return evalExpression(parsedExpression, data)
        }
    }
    if (typeof props === 'function') { return props }
    if (Array.isArray(props)) { return props.map(property => propsResolver(property, data, additionalBlackList)) }

    if (isObject(props)) {
        const fullBlackList = uniq([...blackList, ...additionalBlackList])

        return Object.fromEntries(Object.entries(props).map(([key, property]) => [
            key,
            fullBlackList.includes(key) ? property : propsResolver(property, data, additionalBlackList),
        ]))
    }

    return props
}

export const resolveItem = (item, model) => {
    if (isEmpty(model)) {
        return item
    }

    return propsResolver(item, model)
}
