import React from 'react'
import isEmpty from 'lodash/isEmpty'
import PropTypes from 'prop-types'

import { Html as HtmlSnippet } from '../../snippets/Html/Html'
import evalExpression, { parseExpression } from '../../../utils/evalExpression'

/**
 * Компонент встаквки html-кода производит резолв плейсхолдеров
 * @reactProps {string} url - url html, который будет вставляться
 * @reactProps {string} id - id виджета
 * @reactProps {string} html - html строка
 * @reactProps {object} data - данные
 * <Html id="HtmlWidget" url="/test.html"/>
 */

export const Html = (props) => {
    const { html, data, loading = false, id, className } = props

    if (!html) {
        return null
    }

    const htmlResolver = (html, data) => {
        if (isEmpty(data)) {
            return html
        }

        const parsedExpression = parseExpression(html)

        if (parsedExpression) {
            return evalExpression(parsedExpression.replace(/\n/g, ''), data)
        }

        return html
    }

    const finalHtml = htmlResolver(html, data)

    /* устраняет мерцания с плейсхолдерами */
    if (parseExpression(finalHtml)) {
        return null
    }

    return (
        !loading && (
            <HtmlSnippet
                html={finalHtml}
                id={id}
                className={className}
            />
        )
    )
}

Html.propTypes = {
    url: PropTypes.string,
    id: PropTypes.string,
    html: PropTypes.string,
    data: PropTypes.object,
}

export default Html
