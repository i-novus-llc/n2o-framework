import isEmpty from 'lodash/isEmpty'

import { IModel } from '../components/widgets/Form/fields/MarkdownField/helpers'

import evalExpression, { parseExpression } from './evalExpression'

export const useHtmlResolver = (html: string, model: IModel) => {
    if (!html) {
        return null
    }

    if (isEmpty(model)) {
        return html
    }

    const parsedExpression = parseExpression(html)

    if (parsedExpression) {
        return evalExpression(parsedExpression.replace(/\n/g, ''), model)
    }

    return html
}
