import React from 'react'
import PropTypes from 'prop-types'

import { Html as HtmlSnippet } from '../../snippets/Html/Html'
import { parseExpression } from '../../../utils/evalExpression'

/**
 * Компонент встаквки html-кода производит резолв плейсхолдеров
 * @reactProps {string} url - url html, который будет вставляться
 * @reactProps {string} id - id виджета
 * @reactProps {string} html - html строка
 * @reactProps {object} data - данные
 * <Html id="HtmlWidget" url="/test.html"/>
 */

// Принимает html и data
// прим. html = <h1>User is +name+ +surname+</h1> ,data = [{"name" : "testUserName", "surname": "testUserSurname"}],
// заменяет плейсхолдеры в html (прим. {name}, {surname}) на стоотвствующие значения по ключам в data.

export const replacePlaceholders = (html, data) => {
    if (!html) {
        return null
    }

    const keys = Object.keys(data)

    keys.forEach((key) => {
    // заменяет плейсхолдеры на соответствующие ключи:значения в data
        html = html.replace(new RegExp(`'\\+${key}\\+'`, 'gm'), data[key])
    })

    return html
}

export const Html = (props) => {
    const { html, data, loading = false, id, className } = props

    if (!html) {
        return null
    }

    let finalHtml = html

    if (data) {
        finalHtml = replacePlaceholders(html, data)

        if (parseExpression(finalHtml)) {
            finalHtml = parseExpression(finalHtml)
        }

        if (finalHtml.startsWith('\'') && finalHtml.endsWith('\'')) {
            finalHtml = finalHtml.substring(1, finalHtml.length - 1)
        }
    }

    /* устраняет мерцания с плейсхолдерами */
    if (parseExpression(finalHtml) || !html) {
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
