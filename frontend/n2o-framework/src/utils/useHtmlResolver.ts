import isEmpty from 'lodash/isEmpty'
import { useContext } from 'react'
import DOMPurify, { type Config } from 'dompurify'

import { type State } from '../ducks/user/User'
import { type Model } from '../components/widgets/Form/fields/MarkdownField/helpers'
import { ExpressionContext } from '../core/Expression/Context'
import { parseExpression } from '../core/Expression/parse'
import { executeExpression } from '../core/Expression/execute'
import { N2O_BUTTON_TAG, N2O_BUTTON_ATTR } from '../components/widgets/Form/fields/MarkdownField/MappedComponents'

const BASE_CONFIG = {
    ADD_TAGS: [N2O_BUTTON_TAG],
    ADD_ATTR: N2O_BUTTON_ATTR,
}

export const useHtmlResolver = (html: string, model: Model, csp?: State['csp']) => {
    const evalContext = useContext(ExpressionContext)

    if (!html) { return null }

    // @INFO DOMPurify имеет сложную типизацию конфигурации ADD_TAGS и ADD_ATTR
    // string[] | function(): boolean, за основу взят type string[]
    const finalConfig: Config = csp
        ? {
            ...csp,
            ADD_TAGS: [...BASE_CONFIG.ADD_TAGS, ...csp.ADD_TAGS as string[] || []],
            ADD_ATTR: [...BASE_CONFIG.ADD_ATTR, ...csp.ADD_ATTR as string[] || []],
        }
        : BASE_CONFIG

    const parsedExpression = parseExpression(html)

    if (!parsedExpression) {
        return DOMPurify.sanitize(html, finalConfig)
    }

    if (isEmpty(model)) { return null }

    const resolvedHtml = executeExpression(parsedExpression.replace(/\n/g, '\\n'), model, evalContext) as string

    return DOMPurify.sanitize(resolvedHtml, finalConfig)
}
