import isEmpty from 'lodash/isEmpty'

import { parseExpression } from '../../utils/evalExpression'
// @ts-ignore ignore import error from js file
import propsResolver from '../../utils/propsResolver'

export const textResolver = (
    model: object| object[],
    text: string | null,
): string | null => {
    if (!text) {
        return null
    }

    if (!parseExpression(text)) {
        return text
    }

    if (isEmpty(model)) {
        return null
    }

    const { text: resolvedText } = propsResolver({ text }, model)

    return resolvedText
}
