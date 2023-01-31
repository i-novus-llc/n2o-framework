import React from 'react'
import PropTypes from 'prop-types'
import { Link, NavLink } from 'react-router-dom'
import cx from 'classnames'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import merge from 'lodash/merge'
import { NavItem, UncontrolledDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import colors from '../../../constants/colors'
import { Badge } from '../../../components/snippets/Badge/Badge'
import { LinkTarget } from '../../../constants/linkTarget'
import { NavItemImage } from '../../../components/snippets/NavItemImage/NavItemImage'
import { WithDataSource } from '../../../core/datasource/WithDataSource'
import { resolveItem } from '../../../utils/propsResolver'
import { dataProviderResolver } from '../../../core/dataProviderResolver'

export const getFromSource = (itemProps, datasources, datasource, models) => {
    const props = { ...itemProps }
    const { href, pathMapping = {}, queryMapping = {} } = props

    if (!isEmpty(pathMapping) || !isEmpty(queryMapping)) {
        const { url } = dataProviderResolver(props, { url: href, pathMapping, queryMapping })

        props.href = url
    }

    if (!datasource) {
        return props
    }

    const defaultFromDataSource = get(datasources, `${datasource}.values`, [])
    const initialModel = defaultFromDataSource.reduce((acc, value) => ({ ...acc, ...value }), {})

    if (models && !isEmpty(models.datasource)) {
        return merge(resolveItem(props, initialModel), resolveItem(props, models.datasource))
    }

    if (datasources[datasource]) {
        return resolveItem(props, initialModel)
    }

    return props
}

const NavItemIcon = ({ icon }) => <i className={cx('mr-1', icon)} />

NavItemIcon.propTypes = {
    icon: PropTypes.string,
}

/**
 * Контейнер navItem'ов, в зависимости от type, создает внутри линк, дропдаун или текст
 * @param {object} props - пропсы
 * @param {object} itemProps  - объект, пропсы которого перейдут в item. Например, для ссыллок {id, title, href,type, link, linkType}
 * @param {boolean} active  - active (применять || нет active class)
 */

const NavItemContainer = ({
    itemProps,
    type,
    sidebarOpen,
    options,
    direction,
    active,
    datasources,
    models,
}) => {
    const datasource = get(itemProps, 'datasource')

    const item = getFromSource(itemProps, datasources, datasource, models)

    const getInnerLink = (item, className) => (
        <NavLink
            exact
            className={cx('nav-link', className, { active })}
            to={item.href}
            activeClassName="active"
            target={item.target}
        >
            {!item.imageSrc && item.icon && <NavItemIcon icon={item.icon} />}
            <NavItemImage
                imageSrc={item.imageSrc}
                title={item.title}
                imageShape={item.imageShape}
            />
            {item.title}
        </NavLink>
    )

    const handleLink = (item, className) => {
        const target = item.target === LinkTarget.Application ? LinkTarget.Self : item.target

        if (item.linkType === 'outer') {
            return (
                <NavItem>
                    <a
                        className={cx('nav-link', className)}
                        href={item.href}
                        target={target}
                    >
                        {!item.imageSrc && item.icon && <i className={cx('mr-1', item.icon)} />}
                        <NavItemImage
                            imageSrc={item.imageSrc}
                            title={item.title}
                            imageShape={item.imageShape}
                        />
                        <Badge {...item.badge}>{item.title}</Badge>
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
                    target={target}
                >
                    {!item.imageSrc && item.icon && <NavItemIcon icon={item.icon} />}
                    <NavItemImage
                        imageSrc={item.imageSrc}
                        title={item.title}
                        imageShape={item.imageShape}
                    />
                    <Badge {...item.badge}>{item.title}</Badge>
                </NavLink>
            </NavItem>
        )
    }

    const handleLinkDropdown = (item, dropdownItems, nested) => (
        <UncontrolledDropdown nav inNavbar direction={direction} className={cx({ 'dropdown-level-3': nested })}>
            <DropdownToggle nav caret className={cx({ 'toggle-level-3': nested })}>
                {!item.imageSrc && item.icon && <NavItemIcon icon={item.icon} />}
                <NavItemImage
                    imageSrc={item.imageSrc}
                    title={item.title}
                    imageShape={item.imageShape}
                />
                {item.title}
            </DropdownToggle>
            <DropdownMenu right={get(options, 'right', false)}>
                {dropdownItems}
            </DropdownMenu>
        </UncontrolledDropdown>
    )

    let dropdownItems = []

    if (item.type === 'dropdown' && !sidebarOpen) {
        dropdownItems = item.items.map((child = {}) => {
            if (child.items) {
                const nestedDropdownItems = (
                    <DropdownMenu className="item-level-3" flip>
                        {child.items.map(nestedChild => <DropdownItem>{handleLink(nestedChild, 'dropdown-item')}</DropdownItem>)}
                    </DropdownMenu>
                )

                return (
                    handleLinkDropdown(child, nestedDropdownItems, true)
                )
            }

            return (
                <DropdownItem>{handleLink(child, 'dropdown-item')}</DropdownItem>
            )
        })
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
                <NavItemImage
                    imageSrc={item.imageSrc}
                    title={item.title}
                    imageShape={item.imageShape}
                />
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

    if (item.type === 'dropdown' && !sidebarOpen) {
        if (dropdownItems.length > 0) {
            return handleLinkDropdown(item, dropdownItems)
        }

        return null
    }

    if (item.type === 'link') {
        return handleLink(item)
    }

    if (item.type === 'text') {
        return (
            <NavItem active={active}>
                {!item.imageSrc && item.icon && <NavItemIcon icon={item.icon} />}
                <NavItemImage
                    imageSrc={item.imageSrc}
                    title={item.title}
                    imageShape={item.imageShape}
                />
                <span className="nav-link">{item.title}</span>
            </NavItem>
        )
    }

    return null
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
    itemProps: PropTypes.object,
    active: PropTypes.bool,
    datasources: PropTypes.object,
    models: PropTypes.object,
}

NavItemContainer.defaultProps = {
    direction: 'bottom',
}

export default WithDataSource(NavItemContainer)

export { NavItemContainer }
