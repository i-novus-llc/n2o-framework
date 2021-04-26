import isObject from 'lodash/isObject'
import isPlainObject from 'lodash/isPlainObject'
import values from 'lodash/values'
import isEmpty from 'lodash/isEmpty'

import functions from './functions'
import warning from './warning'

/**
 * Проверяет, является ли строка JS выражением
 * @param value {String} - Проверяемая строка
 * @returns {String|Boolean} - Найденное JS выражение, или false
 */
export function parseExpression(value) {
    const res = String(value).match('^`(.*)`$')

    if (res && res[1]) {
        return res[1]
    }

    return false
}

/**
 * Создает функцию из текста
 * @param args {String[]} - массив имен переменных
 * @param code {String} - код для выполнения
 * @returns {Function} - Функция, созданная из текста code
 */
export function createContextFn(args, code) {
    const joinedArgs = args.join(',')
    const key = `${joinedArgs}|||${code}`

    if (!fooCache[key]) {
        fooCache[key] = new Function(
            windowKeys,
            `return function (${joinedArgs}) { return (${code}) }`,
        )()
    }

    return fooCache[key]
}

const windowKeys = Object.keys(window).filter(v => !v.includes('-'))
const fooCache = {}

function evalExpressionSingle(expression, context, args = context) {
    args = isPlainObject(args) ? args : {}

    try {
        const argsExtended = { ...functions, ...window._n2oEvalContext, ...args }

        const entries = Object.entries(argsExtended)
        const keys = entries.map(arr => arr[0])
        const values = entries.map(arr => arr[1])

        const fn = createContextFn(keys, expression)

        return fn.apply(context || {}, values)
    } catch (e) {
        warning(
            e,
            `Ошибка при выполнение evalExpression! ${e.message}.
      \nВыражение: ${expression}
      \nКонтекст: ${JSON.stringify(context)}`,
        )
    }
}

function evalExpressionMulti(expression, context) {
    let multiContext = context

    if (isObject(multiContext)) {
        multiContext = values(context)
    }

    if (isEmpty(multiContext)) {
        return evalExpressionSingle(expression, multiContext, {})
    }

    return multiContext.every(item => evalExpressionSingle(expression, multiContext, item))
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
    type = { mode: 'single' },
) {
    const { mode } = type

    return mode === 'multi'
        ? evalExpressionMulti(expression, context)
        : evalExpressionSingle(expression, context)
}
