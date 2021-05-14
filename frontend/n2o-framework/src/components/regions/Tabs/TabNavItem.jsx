/**
 * Created by emamoshin on 09.10.2017.
 */
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
                {/* eslint-disable-next-line jsx-a11y/anchor-is-valid */}
                <a
                    onClick={this.handleClick}
                    className={classNames('nav-link', { active, disabled })}
                    href="#"
                >
                    {icon && <span className={icon} />}
                    {' '}
                    {title}
                </a>
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
