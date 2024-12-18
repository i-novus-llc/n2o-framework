import isEmpty from 'lodash/isEmpty'
import { useContext } from 'react'

import { Model } from '../components/widgets/Form/fields/MarkdownField/helpers'
import { ExpressionContext } from '../core/Expression/Context'
import { parseExpression } from '../core/Expression/parse'
import { executeExpression } from '../core/Expression/execute'

export const useHtmlResolver = (html: string, model: Model) => {
    const evalContext = useContext(ExpressionContext)

    if (!html) { return null }

    const parsedExpression = parseExpression(html)

    if (!parsedExpression) { return html }

    if (isEmpty(model)) { return null }

    return executeExpression(parsedExpression.replace(/\n/g, '\\n'), model, evalContext)
}
