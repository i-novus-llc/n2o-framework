import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Нав Таба
 * @reactProps {string} id - id таба
 * @reactProps {boolean} active - активен / неактивен таб
 * @reactProps {string} icon - иконка
 * @reactProps {string} title - текст
 * @reactProps {boolean} disabled - задизейблен таб или нет
 * @reactProps {function} onClick - выполняется при клике на navItem
 */
export class TabNavItem extends React.Component {
    constructor(props) {
        super(props)

        this.handleClick = this.handleClick.bind(this)
    }

    handleClick(e) {
        const { id, onClick, disabled } = this.props

        e.preventDefault()
        if (onClick && !disabled) {
            onClick(e, id)
        }
    }

    render() {
        const { active, disabled, title, icon } = this.props
        const style = disabled ? { cursor: 'not-allowed' } : {}

        return (
            <li style={style} className="nav-item">
                <button
                    type="button"
                    onClick={this.handleClick}
                    className={classNames('nav-link link-button', { active, disabled })}
                >
                    {icon && <span className={icon} />}
                    {' '}
                    {title}
                </button>
            </li>
        )
    }
}

TabNavItem.propTypes = {
    id: PropTypes.string,
    active: PropTypes.bool,
    disabled: PropTypes.bool,
    title: PropTypes.string,
    icon: PropTypes.string,
    onClick: PropTypes.func,
}

export default TabNavItem
