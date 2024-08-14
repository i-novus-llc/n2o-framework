import React from 'react'
import PropTypes from 'prop-types'

import { Html as HtmlSnippet } from '../../snippets/Html/Html'
import { parseExpression } from '../../../utils/evalExpression'
import { useHtmlResolver } from '../../../utils/useHtmlResolver'

/**
 * Компонент встаквки html-кода производит резолв плейсхолдеров
 * @reactProps {string} url - url html, который будет вставляться
 * @reactProps {string} id - id виджета
 * @reactProps {string} html - html строка
 * @reactProps {object} data - данные
 * <Html id="HtmlWidget" url="/test.html"/>
 */

export const Html = ({
    html,
    data,
    id,
    className,
    loading = false,
}) => {
    const resolvedHtml = useHtmlResolver(html, data)

    if (!resolvedHtml) {
        return null
    }

    /* устраняет мерцания с плейсхолдерами */
    if (parseExpression(resolvedHtml)) {
        return null
    }

    return (
        !loading && (
            <HtmlSnippet
                html={resolvedHtml}
                id={id}
                className={className}
            />
        )
    )
}

export default Html
