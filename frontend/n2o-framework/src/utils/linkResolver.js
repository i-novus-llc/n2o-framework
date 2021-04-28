import get from 'lodash/get'
import isNumber from 'lodash/isNumber'
import isUndefined from 'lodash/isUndefined'
import isNil from 'lodash/isNil'
import isBoolean from 'lodash/isBoolean'
import isObject from 'lodash/isObject'
import isEmpty from 'lodash/isEmpty'
import values from 'lodash/values'
import keys from 'lodash/keys'
import some from 'lodash/some'
import every from 'lodash/every'
import isNaN from 'lodash/isNaN'

import evalExpression, { parseExpression } from './evalExpression'

/**
 * Получение значения по ссылке и выражению.
 * @param state
 * @param link
 * @param value
 * @returns {*}
 */
export default function linkResolver(state, { link, value }) {
    const multi = get(state, 'models.multi')
    const hasMultiModel = some(values(multi), model => !isEmpty(model))

    if (!link && isNil(value)) { return }

    if (isBoolean(value)) { return value }
    if (isNumber(value)) { return value }

    const context = get(state, link)
    const isMultiKeys = every(keys(context), key => !isNaN(Number(key)))

    if (isUndefined(value) && link) { return context }

    const json = JSON.stringify(value)

    return JSON.parse(json, (k, val) => {
        const isMulti =
      context &&
      values(context).every(elem => isObject(elem)) &&
      hasMultiModel &&
      isMultiKeys

        const parsedValue = parseExpression(val)

        if (parsedValue) {
            if (isMulti) {
                return evalExpression(parsedValue, Object.values(context))
            }

            return evalExpression(parsedValue, context)
        }

        return val
    })
}
