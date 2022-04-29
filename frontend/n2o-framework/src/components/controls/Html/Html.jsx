import React from 'react'
import PropTypes from 'prop-types'

import { Html as HtmlResolver } from '../../widgets/Html/Html'

/**
 * Компонент поле html
 * @param {string} id - id
 * @param {object} model - className
 * @param {boolean} visible
 * @constructor
 */
export function Html({ visible, model, ...rest }) {
    if (!visible) {
        return null
    }

    return (
        <HtmlResolver {...rest} data={model} />
    )
}

Html.propTypes = {
    id: PropTypes.string,
    html: PropTypes.string,
    className: PropTypes.string,
    visible: PropTypes.bool,
    model: PropTypes.object,
}

export default Html
