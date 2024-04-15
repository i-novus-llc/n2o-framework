import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { parseFormatter } from '@i-novus/n2o-components/lib/utils/parseFormatter'

/**
 * Компонент поле текст
 * @param {string} text - текст поля
 * @param {boolean} visible - флаг видимости
 * @param {string} format - формат текста
 * @param {string} className - класс поля
 * @param {object} style - стили поля
 * @constructor
 */
function TextField({ text, visible, format, className, style = {} }) {
    /* контроль переносов строк и пробелов осуществляется через xml */
    const currentStyle = { ...style, whiteSpace: 'break-spaces' }

    return (
        visible && (
            <div
                className={classNames('n2o-text-field n2o-snippet', {
                    [className]: className,
                })}
                style={currentStyle}
            >
                {format ? parseFormatter(text, format) : text}
            </div>
        )
    )
}

TextField.propTypes = {
    text: PropTypes.string,
    visible: PropTypes.bool,
    format: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
}

TextField.defaultProps = {
    visible: true,
}

export default TextField
