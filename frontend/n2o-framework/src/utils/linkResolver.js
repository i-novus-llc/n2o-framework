import get from 'lodash/get'
import isNumber from 'lodash/isNumber'
import isUndefined from 'lodash/isUndefined'
import isNil from 'lodash/isNil'
import isBoolean from 'lodash/isBoolean'
import forOwn from 'lodash/forOwn'
import isObject from 'lodash/isObject'
import isArray from 'lodash/isArray'

import evalExpression, { parseExpression } from './evalExpression'

/**
 * Рекурсивно проходит по объекту и резолвит все link
 * @param object
 * @param state
 * @returns {{}}
 */
export function resolveLinksRecursively(object = {}, state = {}) {
    const resolvedObject = {}

    forOwn(object, (v, k) => {
        if (isObject(v) && !isArray(v)) {
            resolvedObject[k] = !v.link ? resolveLinksRecursively(v, state) : linkResolver(state, v)
        } else {
            resolvedObject[k] = v
        }
    })

    return resolvedObject
}

/**
 * Получение значения по ссылке и выражению.
 * @param {Object} state
 * @param {Object} params
 * @param {string} [params.link]
 * @param [params.value]
 * @returns {*}
 */
export default function linkResolver(state, { link, value }, evalContext = {}) {
    if (!link && isNil(value)) { return undefined }
    if (isBoolean(value)) { return value }
    if (isNumber(value)) { return value }

    const model = get(state, link)

    if (isUndefined(value) && link) { return model }

    const json = JSON.stringify(value)

    return JSON.parse(json, (k, val) => {
        const expression = parseExpression(val)

        if (expression) {
            return evalExpression(expression, model, evalContext)
        }

        return val
    })
}
