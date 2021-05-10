import React from 'react'
import PropTypes from 'prop-types'

import { statusTextContainerStyle, statusTextIconStyle } from './utils'

/**
 * Сниппет StatusText
 * @reactProps {string} text - текст статуса
 * @reactProps {string} textPosition - позиция текста right / left
 * @reactProps {string} className - custom className контейнера
 * @reactProps {string} color - цветовая схема бейджа(["primary", "secondary", "success", "danger", "warning", "info", "light", "dark", "white"])
 * @example
 * <StatusText text={'Test text'} className={'test-class'} color={'success'}/>
 */

function StatusText(props) {
    const { text, textPosition, className, color } = props

    return (
        <div className={statusTextContainerStyle(textPosition, className)}>
            {color && <span className={statusTextIconStyle(textPosition, color)} />}
            {text && <p className="n2o-status-text_text">{text}</p>}
        </div>
    )
}

StatusText.propTypes = {
    /**
   * текст статуса
   */
    text: PropTypes.string,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Цвет иконки статуса
   */
    color: PropTypes.oneOf([
        'primary',
        'secondary',
        'success',
        'danger',
        'warning',
        'info',
        'light',
        'dark',
        'white',
    ]),
}

StatusText.defaultProps = {
    textPosition: 'right',
}

export default StatusText
