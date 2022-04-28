import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { Html as HtmlSnippet } from '../../snippets/Html/Html'

/**
 * Компонент поле html
 * @param {string} id - id
 * @param {string} html - html
 * @param {string} className - className
 * @param {boolean} visible
 * @constructor
 */
export function Html({ id, html, className, visible }) {
    if (!visible) {
        return null
    }

    return (
        <HtmlSnippet
            id={id}
            html={html}
            className={
                classNames(
                    'n2o-html-field',
                    className,
                )
            }
        />
    )
}

Html.propTypes = {
    id: PropTypes.string,
    html: PropTypes.string,
    className: PropTypes.string,
    visible: PropTypes.bool,
}

export default Html
