import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Компонент иконки
 * @reactProps {string} className - имя css класса блока
 * @reactProps {object} style - css стили блока
 * @reactProps {boolean} disabled - флаг неактивности иконки
 * @reactProps {string} name - класс иконки
 * @reactProps {boolean} spin - флаг вращения иконки
 * @reactProps {boolean} bordered - флаг рамки вокруг иконки
 * @reactProps {boolean} circular - флаг закругления вокруг иконки
 */
export function Icon({ name, className, disabled, spin, circular, bordered, style }) {
    const iconClass = classNames({
        'n2o-icon': true,
        [name]: name,
        [className]: className,
        disabled,
        'fa-spin': spin,
        circular,
        bordered,
    })

    return <i className={iconClass} style={style} />
}

Icon.propTypes = {
    /**
     * Класс иконки
     */
    className: PropTypes.string,
    /**
     * Стили иконки
     */
    style: PropTypes.object,
    /**
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
     * Название иконки
     */
    name: PropTypes.string.isRequired,
    /**
     * Флаг вращения иконки
     */
    spin: PropTypes.bool,
    /**
     * Флаг рамки вокруг иконки
     */
    bordered: PropTypes.bool,
    /**
     * Флаг закругления иконки
     */
    circular: PropTypes.bool,
}

Icon.defaultProps = {
    disabled: false,
    spin: false,
    bordered: false,
    circular: false,
}

export default Icon
