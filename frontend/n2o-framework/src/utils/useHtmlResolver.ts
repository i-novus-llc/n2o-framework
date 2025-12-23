import isEmpty from 'lodash/isEmpty'
import { useContext } from 'react'
import DOMPurify from 'dompurify'

import { Model } from '../components/widgets/Form/fields/MarkdownField/helpers'
import { ExpressionContext } from '../core/Expression/Context'
import { parseExpression } from '../core/Expression/parse'
import { executeExpression } from '../core/Expression/execute'
import { N2O_BUTTON_TAG, N2O_BUTTON_ATTR } from '../components/widgets/Form/fields/MarkdownField/MappedComponents'

export const SANITIZE_CONFIG = {
    ADD_TAGS: [N2O_BUTTON_TAG],
    ADD_ATTR: [...N2O_BUTTON_ATTR],
}

export const useHtmlResolver = (html: string, model: Model) => {
    const evalContext = useContext(ExpressionContext)

    if (!html) { return null }

    const parsedExpression = parseExpression(html)

    if (!parsedExpression) {
        return DOMPurify.sanitize(html, SANITIZE_CONFIG)
    }

    if (isEmpty(model)) { return null }

    const resolvedHtml = executeExpression(parsedExpression.replace(/\n/g, '\\n'), model, evalContext) as string

    return DOMPurify.sanitize(resolvedHtml, SANITIZE_CONFIG)
}
