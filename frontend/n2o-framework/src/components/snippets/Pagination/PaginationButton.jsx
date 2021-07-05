/**
 * Created by emamoshin on 21.09.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Компонент PaginationButton
 * @reactProps {boolean} active - текущая страница
 * @reactProps {boolean} disabled - флаг неактивноти кнопки пагинации
 * @reactProps {boolean} noBorder - флаг включения/выключения бордера
 * @reactProps {function} onSelect - функция клика по кнопке пагинации
 * @reactProps {number} eventKey
 * @reactProps {string|number} label - текст внутри кнопки пагинации
 */
export const PaginationButton = (props) => {
    const { disabled, onSelect, eventKey, label, active, noBorder, tabIndex } = props

    const handleClick = (e) => {
        e.preventDefault()

        if (disabled) {
            return
        }

        if (onSelect) {
            onSelect(eventKey, e)
        }
    }

    return (
        // eslint-disable-next-line jsx-a11y/no-noninteractive-element-interactions
        <li
            className={classNames('page-item', { active, disabled })}
            onClick={handleClick}
        >
            {/* eslint-disable-next-line jsx-a11y/anchor-is-valid,jsx-a11y/control-has-associated-label */}
            <a
                className={classNames('page-link', noBorder ? 'no-border' : '')}
                href="#"
                tabIndex={tabIndex}
            >
                { label }
            </a>
        </li>
    )
}

PaginationButton.propTypes = {
    active: PropTypes.bool,
    tabIndex: PropTypes.number,
    disabled: PropTypes.bool,
    noBorder: PropTypes.bool,
    onSelect: PropTypes.func,
    eventKey: PropTypes.number,
    label: PropTypes.oneOfType([PropTypes.string, PropTypes.number, PropTypes.element]),
}

export default PaginationButton
