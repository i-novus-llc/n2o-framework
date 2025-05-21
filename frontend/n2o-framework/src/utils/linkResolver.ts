import get from 'lodash/get'
import isNumber from 'lodash/isNumber'
import isUndefined from 'lodash/isUndefined'
import isNil from 'lodash/isNil'
import isBoolean from 'lodash/isBoolean'

import { State } from '../ducks/State'

import { evalExpression, parseExpression } from './evalExpression'

interface LinkProps {
    link: string,
    value?: string
}
/**
 * Получение значения по ссылке и выражению.
 */
export function linkResolver(state: State, { link, value }: LinkProps, evalContext?: Record<string, unknown>) {
    if (!link && isNil(value)) { return undefined }
    if (isBoolean(value)) { return value }
    if (isNumber(value)) { return value }

    const model = get(state, link)

    if (isUndefined(value) && link) { return model }

    const json = JSON.stringify(value)

    return JSON.parse(json, (k, val) => {
        const expression = parseExpression(val)

        if (expression) {
            return evalExpression(expression, model, evalContext || {})
        }

        return val
    })
}

export default linkResolver
