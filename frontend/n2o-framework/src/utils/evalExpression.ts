import { isPlainObject, values, isEmpty } from 'lodash'

// @ts-ignore ignore import error from js file
import functions from './functions'
// @ts-ignore ignore import error from js file
import warning from './warning'

type ExpressionFunction = <TReturn = unknown>(...context: unknown[]) => TReturn

/**
 * Проверяет, является ли строка JS выражением
 * @param value {String} - Проверяемая строка
 * @returns {String|Boolean} - Найденное JS выражение, или false
 */
export function parseExpression(value: unknown): false | string {
    if (typeof value !== 'string') { return false }

    if (value.startsWith('`') && value.endsWith('`')) {
        return value.substring(1, value.length - 1)
    }

    return false
}

/**
 * Получение глобального контекста
 */
export function getGlobal(): typeof globalThis {
    // eslint-disable-next-line no-undef
    if (typeof globalThis !== 'undefined') { return globalThis }
    // eslint-disable-next-line no-restricted-globals
    if (typeof self !== 'undefined') { return self }
    if (typeof window !== 'undefined') { return window }
    // @ts-ignore FIXME: скорее всего уже можно обойтись одним globalThis
    if (typeof global !== 'undefined') { return global }

    // @ts-ignore FIXME: скорее всего уже можно обойтись одним globalThis
    return (function getThis() { return this }())
}

/**
 * Получение обёртки над глобальым контекстом для контроля доступа к пропертям
 */
export const createGlobalContext = (() => {
    let context: object | undefined
    const allowList: Array<string | symbol> = [
        'undefined', 'null', 'NaN', 'Infinity', 'console', 'JSON', 'crypto', 'Math',
        // тут сомнительные пропсы, но пусть пока будут, будем убирать по мере разбора
        'location', 'history', 'navigator',
    ]
    const selfKeys: Array<string | symbol> = ['window', 'self', 'global', 'globalThis']

    return function createGlobalContext(): object {
        if (context) { return context }

        const self = getGlobal()

        context = new Proxy(self, {
            has() { return true },
            set() { return false },
            get(target, key) {
                // @ts-ignore FIXME проставить нормальный тип для контекста
                const value = target[key]

                if (typeof value === 'function' || allowList.includes(key)) {
                    return value
                }
                if (selfKeys.includes(key)) {
                    return context
                }

                return undefined
            },
        })

        return context
    }
})()

const expressionCache = new Map<string, ExpressionFunction>()

/**
 * Создает функцию из текста
 * @param args {String[]} - массив имен переменных
 * @param code {String} - код для выполнения
 * @returns {Function} - Функция, созданная из текста code
 */
export function createContextFn(args: string[], code: string): ExpressionFunction {
    const joinedArgs = args.join(',')
    const key = `${joinedArgs}|||${code}`

    const expressionFunction = expressionCache.get(key)

    if (expressionFunction) { return expressionFunction }

    // eslint-disable-next-line no-new-func
    const creator = new Function(
        'globalContext',
        `with(globalContext) {
            return function (${joinedArgs}) { return (${code}) }
        }`,
    ) as (global: object) => ExpressionFunction
    const func: ExpressionFunction = creator(createGlobalContext())

    expressionCache.set(key, func)

    return func
}

// eslint-disable-next-line consistent-return
function evalExpressionSingle(expression: string, context: object = {}, args = context) {
    if (expression === 'false') {
        return false
    }
    if (expression === 'true') {
        return true
    }

    args = isPlainObject(args) ? args : {}

    try {
        // @ts-ignore _n2oEvalContext задаётся где-то в App. FIXME: переделать на явную передачу контекста
        // eslint-disable-next-line no-underscore-dangle
        const argsExtended = { ...functions, ...window._n2oEvalContext, ...args }

        const entries = Object.entries(argsExtended)
        const keys = entries.map(arr => arr[0])
        const values = entries.map(arr => arr[1])

        const fn = createContextFn(keys, expression)

        return fn.apply(context, values)
    } catch (error) {
        warning(
            true,
            `Ошибка при выполнение evalExpression! ${error instanceof Error ? error.message : error}.
      \nВыражение: ${expression}
      \nКонтекст: ${JSON.stringify(context)}`,
        )
    }
}

function evalExpressionMulti(expression: string, context: object | object[]) {
    const multiContext = Array.isArray(context) ? context : values(context)

    if (isEmpty(multiContext)) {
        return evalExpressionSingle(expression, multiContext, {})
    }

    return multiContext.every(item => evalExpressionSingle(expression, multiContext, item))
}

// TODO вынести отсюда мульти в отдельный файл. Вообще не понятно зачем он тут, нужен для конкретного кейса
/**
 * Выполняет JS выражение
 * @param expression {String} - Выражение, которое нужно выполнить
 * @param context - {Object} - Аргумент вызова (будет обогощен либами, типа lodash, moment и пр.)
 * @param type - {Object} - настройка evalExpression (mode = multi || mode = single default)
 * @returns {*} - результат вычисления
 */
export default function evalExpression(
    expression: string,
    context: object,
    type = { mode: 'single' },
) {
    const { mode } = type

    return mode === 'multi'
        ? evalExpressionMulti(expression, context)
        : evalExpressionSingle(expression, context)
}
