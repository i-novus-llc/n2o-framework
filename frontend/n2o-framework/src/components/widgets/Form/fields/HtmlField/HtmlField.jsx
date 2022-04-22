import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { Html } from '../../../../snippets/Html/Html'

/**
 * Компонент поле html
 * @param {string} id - id
 * @param {string} html - html
 * @param {string} className - className
 * @constructor
 */
export function HtmlField({ id, html, className }) {
    return (
        <Html
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

HtmlField.propTypes = {
    id: PropTypes.string,
    html: PropTypes.string,
    className: PropTypes.string,
}
