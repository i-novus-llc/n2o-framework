import get from 'lodash/get'
import isNumber from 'lodash/isNumber'
import isUndefined from 'lodash/isUndefined'
import isNil from 'lodash/isNil'
import isBoolean from 'lodash/isBoolean'

import evalExpression, { parseExpression } from './evalExpression'

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
