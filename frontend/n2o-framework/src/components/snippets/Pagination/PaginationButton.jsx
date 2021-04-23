/**
 * Created by emamoshin on 21.09.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

/**
 * Компонент PaginationButton
 * @reactProps {boolean} active - текущая страница
 * @reactProps {boolean} disabled - флаг неактивноти кнопки пагинации
 * @reactProps {boolean} noBorder - флаг включения/выключения бордера
 * @reactProps {function} onSelect - функция клика по кнопке пагинации
 * @reactProps {number} eventKey
 * @reactProps {string|number} label - текст внутри кнопки пагинации
 */
class PaginationButton extends React.Component {
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
            <li
                className={cx('page-item', { active, disabled })}
                onClick={this.handleClick}
            >
                <a
                    className={cx('page-link', noBorder ? 'no-border' : '')}
                    href="#"
                    dangerouslySetInnerHTML={{ __html: label }}
                    tabIndex={tabIndex}
                />
            </li>
        )
    }
}

PaginationButton.propTypes = {
    active: PropTypes.bool,
    disabled: PropTypes.bool,
    noBorder: PropTypes.bool,
    onSelect: PropTypes.func,
    eventKey: PropTypes.number,
    label: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
}

export default PaginationButton
