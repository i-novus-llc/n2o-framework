import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'

type OmitEmpty<T> = {
    [K in keyof T]: T[K] extends null | undefined | ''
        ? never
        : T[K] extends Record<PropertyKey, unknown>
            ? OmitEmpty<T[K]> extends infer Sub
                ? {} extends Sub
                    ? never
                    : Sub
                : never
            : T[K] extends readonly unknown[]
                ? T[K]['length'] extends 0
                    ? never
                    : T[K]
                : T[K]
}

/**
 * Удаляет все пустые значения в параметрах запроса
 * @param obj
 * @returns {*}
 */
export function clearEmptyParams<
    T extends object,
    R extends OmitEmpty<T> = OmitEmpty<T>,
>(obj: T): R {
    const result: Record<string, unknown> = {}

    for (const [key, value] of Object.entries(obj)) {
        if (isNil(value) || value === '') { continue }
        if (Array.isArray(value) && value.length) {
            result[key] = value
        } else if (typeof value === 'object') {
            const sub = clearEmptyParams(value)

            if (!isEmpty(sub)) { result[key] = sub }
        } else {
            result[key] = value
        }
    }

    return result as R
}
