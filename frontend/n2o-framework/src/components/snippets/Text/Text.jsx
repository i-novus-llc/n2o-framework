/**
 * Created by emamoshin on 03.10.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'

import parseFormatter from '../../../utils/parseFormatter'

/**
 * Компонент, который отображает текст
 * @reactProps {string} text - текст
 * @reactProps {string} format - формат времени
 * @reactProps {string} className - класс для элемета Text
 * @reactProps {boolean} preLine - флаг переноса текста
 */
function Text({ text, format, preLine, className, ...rest }) {
    return (
        <span
            className={cn(className, {
                'white-space-pre-line': preLine,
            })}
            {...rest}
        >
            {parseFormatter(text, format)}
        </span>
    )
}

Text.propTypes = {
    /**
   * Текст компонента
   */
    text: PropTypes.string,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Формат текста
   */
    format: PropTypes.string,
    /**
   * Флаг сохранения переносов текста
   */
    preLine: PropTypes.bool,
}

Text.defaultProps = {
    preLine: false,
}

export default Text
