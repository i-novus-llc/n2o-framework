import isObject from 'lodash/isObject'
import uniq from 'lodash/uniq'

import { parseExpression } from './parse'
import { executeExpression } from './execute'

const blackList = [
    'dataProvider',
    'action',
    'actions',
    'queryMapping',
    'pathMapping',
]

type _Map<T> = {
    [K in keyof T]: Resolve<T[K]>
}
export type Resolve<T> = T extends readonly unknown[]
    ? _Map<T>
    : T extends Record<PropertyKey, unknown>
        ? _Map<T>
        : T extends `\`${string}\``
            ? unknown
            : T

// FIXME: Разобраться почему он нормально не понимает тип аргумента и результата, убрать "as Resolve<T>"
export function propsResolver<
    Resolved extends Resolve<Prop>,
    Prop = unknown,
>(
    prop: Prop,
    model: Record<string, unknown> | Array<Record<string, unknown>> = {},
    expressionContext: Record<string, unknown> = {},
    ignoreKeys?: string[],
): Resolved {
    const ignore = ignoreKeys ? uniq([...blackList, ...ignoreKeys]) : blackList

    function resolve<T>(prop: T): Resolve<T> {
        if (!prop) { return prop as Resolve<T> }
        if (typeof prop === 'string') {
            const parsedExpression = parseExpression(prop)

            if (parsedExpression) {
                return executeExpression(parsedExpression, model, expressionContext)
            }
        }
        if (typeof prop === 'function') { return prop as Resolve<T> }
        if (Array.isArray(prop)) {
            return prop.map(resolve) as Resolve<T>
        }

        if (isObject(prop)) {
            return Object.fromEntries(Object.entries(prop).map(([key, property]) => [
                key,
                ignore.includes(key) ? property : resolve(property),
            ])) as Resolve<T>
        }

        return prop as Resolve<T>
    }

    return resolve(prop) as Resolved
}
