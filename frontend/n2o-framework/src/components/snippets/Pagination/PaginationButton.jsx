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
export class PaginationButton extends React.Component {
    constructor(props) {
        super(props)
        this.handleClick = this.handleClick.bind(this)
    }

    handleClick(e) {
        e.preventDefault()

        const { disabled, onSelect, eventKey } = this.props

        if (disabled) {
            return
        }

        if (onSelect) {
            onSelect(eventKey, e)
        }
    }

    render() {
        const { label, active, disabled, noBorder, tabIndex } = this.props

        return (
            // eslint-disable-next-line jsx-a11y/no-noninteractive-element-interactions
            <li
                className={classNames('page-item', { active, disabled })}
                onClick={this.handleClick}
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
}

PaginationButton.propTypes = {
    active: PropTypes.bool,
    tabIndex: PropTypes.number,
    disabled: PropTypes.bool,
    noBorder: PropTypes.bool,
    onSelect: PropTypes.func,
    eventKey: PropTypes.number,
    label: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
}

export default PaginationButton
