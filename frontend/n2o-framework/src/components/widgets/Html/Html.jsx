import React from 'react'
import PropTypes from 'prop-types'

/**
 * Компонент встаквки html-кода
 * @reactProps {string} url - url html, который будет вставляться
 * @reactProps {string} id - id виджета
 * @reactProps {string} html - html строка
 * @reactProps {object} data - данные
 * <Html id="HtmlWidget" url="/test.html"/>
 */

// Принимает html и data
// прим. html = <h1>User is {name} {surname}</h1> ,data = [{"name" : "testUserName", "surname": "testUserSurname"}],
// заменяет плейсхолдеры в html (прим. {name}, {surname}) на стоотвствующие значения по ключам в data.
// при отсутствии ключа в data или плейсхолдер === {} заменяет на пустоту

export const replacePlaceholders = (html, data) => {
    const keys = Object.keys(data)

    keys.forEach((key) => {
    // заменяет плейсхолдеры на соответствующие ключи:значения в data
        html = html.replace(new RegExp(`{${key}}`, 'gm'), data[key])
    })

    // удаляет остальные плейсхолдеры, включая {}
    return html.replace(new RegExp('{.*?}', 'gm'), '')
}

const Html = (props) => {
    const { html, data, loading = false } = props

    return (
        !loading && (
            <div
                dangerouslySetInnerHTML={{
                    __html: html && data ? replacePlaceholders(html, data) : html,
                }}
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
