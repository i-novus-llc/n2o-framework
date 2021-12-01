import React from 'react'
import PropTypes from 'prop-types'
import { Link, NavLink } from 'react-router-dom'
import cx from 'classnames'
import get from 'lodash/get'
import Badge from 'reactstrap/lib/Badge'
import NavItem from 'reactstrap/lib/NavItem'
import UncontrolledDropdown from 'reactstrap/lib/UncontrolledDropdown'
import DropdownToggle from 'reactstrap/lib/DropdownToggle'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import DropdownItem from 'reactstrap/lib/DropdownItem'

/**
 * Контейнер navItem'ов, в зависимости от type, создает внутри линк, дропдаун или текст
 * @param {object} props - пропсы
 * @param {object} props.item  - объект, пропсы которого перейдут в item. Например, для ссыллок {id, title, href,type, link, linkType}
 * @param {boolean} props.active  - active (применять || нет active class)
 * @param {string} props.  - id активного item'a
 */
const NavItemContainer = ({
    item,
    type,
    sidebarOpen,
    options,
    direction,
    active,
}) => {
    // eslint-disable-next-line react/prop-types
    const NavItemIcon = ({ icon }) => <i className={cx('mr-1', icon)} />

    const getInnerLink = (item, className) => (
        <NavLink
            exact
            className={cx('nav-link', className, { active })}
            to={item.href}
            activeClassName="active"
            target={item.target}
        >
            {item.icon && <NavItemIcon icon={item.icon} />}
            {item.title}
        </NavLink>
    )

    const renderBadge = item => (
        <Badge color={item.badgeColor}>{item.badge}</Badge>
    )

    const handleLink = (item, className) => {
        if (item.linkType === 'outer') {
            return (
                <NavItem>
                    <a
                        className={cx('nav-link', className)}
                        href={item.href}
                        target={item.target}
                    >
                        {item.icon && <i className={cx('mr-1', item.icon)} />}
                        {item.title}
                    </a>
                    {renderBadge(item)}
                </NavItem>
            )
        }

        return (
            <NavItem>
                <NavLink
                    exact
                    className={cx('nav-link', className, { active })}
                    to={item.href}
                    activeClassName="active"
                    target={item.target}
                >
                    {item.icon && <NavItemIcon icon={item.icon} />}
                    {item.title}
                </NavLink>
                {renderBadge(item)}
            </NavItem>
        )
    }

    const handleLinkDropdown = (item, dropdownItems) => (
        <UncontrolledDropdown nav inNavbar direction={direction}>
            <DropdownToggle nav caret>
                {item.icon && <NavItemIcon icon={item.icon} />}
                {item.title}
            </DropdownToggle>
            <DropdownMenu right={get(options, 'right', false)}>
                {dropdownItems}
            </DropdownMenu>
        </UncontrolledDropdown>
    )

    let dropdownItems = []

    if (item.type === 'dropdown' && !sidebarOpen) {
        dropdownItems = item.items.map(child => (
            <DropdownItem>{handleLink(child, 'dropdown-item')}</DropdownItem>
        ))
        if (
            item.type === 'dropdown' &&
            item.items.length > 1 &&
            type === 'sidebar'
        ) {
            dropdownItems = [
                <DropdownItem key={-1} onClick={e => e.preventDefault()}>
                    {item.icon && <NavItemIcon icon={item.icon} />}
                    <a className="dropdown-item">{item.title}</a>
                </DropdownItem>,
                ...dropdownItems,
            ]
        }
    } else if (type === 'sidebar' && item.type === 'dropdown' && sidebarOpen) {
        const defaultLink = item => (
            <Link className="dropdown-item" to={item.href} target={item.target}>
                {item.icon && <NavItemIcon icon={item.icon} />}
                {item.title}
            </Link>
        )
        const linkItem = item => (item.linkType === 'outer'
            ? defaultLink(item)
            : getInnerLink(item, 'dropdown-item'))

        dropdownItems = item.items.map(() => (
            <DropdownItem>
                {' '}
                {linkItem(item)}
                {' '}
            </DropdownItem>
        ))
    }

    return (
        (item.type === 'dropdown' && !sidebarOpen && handleLinkDropdown(item, dropdownItems)) ||
        (item.type === 'link' && handleLink(item)) ||
        (item.type === 'text' && (
            <NavItem active={active}>
                {item.icon && <NavItemIcon icon={item.icon} />}
                <span className="nav-link">{item.title}</span>
            </NavItem>
        )) ||
        null
    )
}

NavItemContainer.propTypes = {
    item: PropTypes.shape({
        title: PropTypes.string,
        href: PropTypes.string,
        icon: PropTypes.string,
        linkType: PropTypes.oneOf(['inner', 'outer']),
        withSubMenu: PropTypes.bool,
        items: PropTypes.array,
    }),
    type: PropTypes.oneOf(['header', 'sidebar']),
    open: PropTypes.bool,
    direction: PropTypes.string,
}

NavItemContainer.defaultProps = {
    direction: 'bottom',
}

export default NavItemContainer
