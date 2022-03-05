import React from 'react'
import { NavLink } from 'react-router-dom'
import classNames from 'classnames'

import { id as generateId } from '../../utils/id'
import { NavItemImage } from '../../components/snippets/NavItemImage/NavItemImage'
import { renderBadge } from '../../components/snippets/Badge/Badge'
import { Tooltip } from '../../components/snippets/Tooltip/Tooltip'

import { Icon, Title, getCurrentTitle } from './utils'
import { linkTypes } from './LinkTypes'

const ItemHOC = ({ children, needTooltip, hint }) => {
    if (needTooltip) {
        return <Tooltip label={children} placement="right" hint={hint} />
    }

    return children
}

export function InnerLink({
    href,
    title,
    icon,
    imageSrc,
    imageShape,
    isMiniView,
    type,
    sidebarOpen,
    isStaticView,
    showContent,
    item,
}) {
    const id = generateId()
    const currentTitle = getCurrentTitle(isMiniView, icon, title, imageSrc)

    const Component = () => (
        <>
            <NavLink
                exact
                to={href}
                className="n2o-sidebar__item"
                activeClassName="active"
                id={id}
            >
                <Icon icon={icon} title={title} type={type} sidebarOpen={sidebarOpen} />
                <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
                <Title
                    title={currentTitle}
                    className={
                        classNames(
                            'n2o-sidebar__item-title',
                            { visible: isStaticView ? true : showContent },
                        )
                    }
                />
                {renderBadge(item)}
            </NavLink>
        </>
    )

    return (
        <ItemHOC needTooltip={isMiniView} hint={title}>
            <Component />
        </ItemHOC>
    )
}

InnerLink.propTypes = linkTypes
