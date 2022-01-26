import React from 'react'
import PropTypes from 'prop-types'
import { Link, NavLink } from 'react-router-dom'
import cx from 'classnames'
import get from 'lodash/get'
import { NavItem, UncontrolledDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import colors from '../../../constants/colors'
import { renderBadge } from '../../../components/snippets/Badge/Badge'
import { NavItemImage } from '../../../components/snippets/NavItemImage/NavItemImage'

/**
 * Контейнер navItem'ов, в зависимости от type, создает внутри линк, дропдаун или текст
 * @param {object} props - пропсы
 * @param {object} props.item  - объект, пропсы которого перейдут в item. Например, для ссыллок {id, title, href,type, link, linkType}
 * @param {boolean} props.active  - active (применять || нет active class)
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
            {!item.imageSrc && item.icon && <NavItemIcon icon={item.icon} />}
            {item.imageSrc && (
                <NavItemImage
                    imageSrc={item.imageSrc}
                    title={item.title}
                    imageShape={item.imageShape}
                />
            )}
            {item.title}
        </NavLink>
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
                        {!item.imageSrc && item.icon && <i className={cx('mr-1', item.icon)} />}
                        {item.imageSrc && (
                            <NavItemImage
                                imageSrc={item.imageSrc}
                                title={item.title}
                                imageShape={item.imageShape}
                            />
                        )}
                        {item.title}
                        {renderBadge(item)}
                    </a>
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
                    {!item.imageSrc && item.icon && <NavItemIcon icon={item.icon} />}
                    {item.imageSrc && (
                        <NavItemImage
                            imageSrc={item.imageSrc}
                            title={item.title}
                            imageShape={item.imageShape}
                        />
                    )}
                    {item.title}
                    {renderBadge(item)}
                </NavLink>
            </NavItem>
        )
    }

    const handleLinkDropdown = (item, dropdownItems) => (
        <UncontrolledDropdown nav inNavbar direction={direction}>
            <DropdownToggle nav caret>
                {!item.imageSrc && item.icon && <NavItemIcon icon={item.icon} />}
                {item.imageSrc && (
                    <NavItemImage
                        imageSrc={item.imageSrc}
                        title={item.title}
                        imageShape={item.imageShape}
                    />
                )}
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
                {!item.imageSrc && item.icon && <NavItemIcon icon={item.icon} />}
                {item.imageSrc && (
                    <NavItemImage
                        imageSrc={item.imageSrc}
                        title={item.title}
                        imageShape={item.imageShape}
                    />
                )}
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
                    {!item.imageSrc && item.icon && <NavItemIcon icon={item.icon} />}
                    { item.imageSrc && (
                        <NavItemImage
                            imageSrc={item.imageSrc}
                            title={item.title}
                            imageShape={item.imageShape}
                        />
                    )}
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
        badge: PropTypes.string,
        badgeColor: PropTypes.oneOf(colors),
        type: PropTypes.oneOf(['dropdown', 'link', 'text']),
        target: PropTypes.string,
        imageSrc: PropTypes.string,
    }),
    type: PropTypes.oneOf(['header', 'sidebar']),
    sidebarOpen: PropTypes.bool,
    direction: PropTypes.string,
    options: PropTypes.object,
}

NavItemContainer.defaultProps = {
    direction: 'bottom',
}

export default NavItemContainer
