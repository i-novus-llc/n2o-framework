import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Status - badge с иконкой и тексом
 * @reactProps {string} text - текст
 * @reactProps {string} icon - иконка
 * @reactProps {string} className - кастом класс компонента
 * @reactProps {string} iconDirection - позиция иконки относительно текста (default = left, right)
 */

/**
 *
 * @param text
 * @param icon
 * @param className
 * @param iconDirection
 * @returns {JSX.Element}
 */

export default function Status({
    text,
    icon,
    className,
    iconDirection = 'left',
}) {
    if (!icon && !text) {
        return null
    }

    return (
        <div
            className={classNames('n2o-status', className, {
                'status-icon-left': iconDirection === 'left',
                'status-icon-right': iconDirection === 'right',
            })}
        >
            {icon && (
                <i
                    className={classNames('n2o-status__icon', icon, {
                        'with-text': text,
                    })}
                />
            )}
            {text && (
                <div className={classNames('n2o-status__text', { 'with-icon': icon })}>
                    {text}
                </div>
            )}
        </div>
    )
}

Status.propTypes = {
    /**
   * text
   */
    text: PropTypes.string,
    /**
   * icon
   */
    icon: PropTypes.string,
    /**
   * className
   */
    className: PropTypes.string,
    /**
   * iconDirection
   */
    iconDirection: PropTypes.string,
}
