import React from 'react'

import { Html as HtmlSnippet } from '../../snippets/Html/Html'
import { parseExpression } from '../../../utils/evalExpression'
import { useHtmlResolver } from '../../../utils/useHtmlResolver'

export type Props = {
    id: string
    html: string
    data: Record<string, unknown>
    className?: string
    loading?: boolean
}

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
}: Props) => {
    const resolvedHtml = useHtmlResolver(html, data) as string

    if (!resolvedHtml) {
        return null
    }

    /* устраняет мерцания с плейсхолдерами */
    if (parseExpression(resolvedHtml)) {
        return null
    }

    if (loading) {
        return null
    }

    return <HtmlSnippet html={resolvedHtml} id={id} className={className} />
}

export default Html
