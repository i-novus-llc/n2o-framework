import isEmpty from 'lodash/isEmpty'

import { parseExpression } from '../../utils/evalExpression'
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

    // @ts-ignore import from js file
    const { text: resolvedText } = propsResolver({ text }, model)

    return resolvedText
}
