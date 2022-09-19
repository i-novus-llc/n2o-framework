import React from 'react'
import { NavLink } from 'react-router-dom'
import classNames from 'classnames'

import { id as generateId } from '../../utils/id'
import { NavItemImage } from '../../components/snippets/NavItemImage/NavItemImage'
import { Badge } from '../../components/snippets/Badge/Badge'
import { ExtendedTooltipComponent } from '../../components/snippets/Tooltip/TooltipHOC'

import { Icon, Title, getCurrentTitle } from './utils'
import { linkTypes } from './LinkTypes'

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

    const hint = isMiniView ? title : null

    return (
        <ExtendedTooltipComponent
            hint={hint}
            placement="right"
        >
            <NavLink
                exact
                to={href}
                className="n2o-sidebar__item"
                activeClassName="active"
                id={id}
            >
                <Icon icon={icon} title={title} type={type} sidebarOpen={sidebarOpen} />
                <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
                <Badge {...item.badge}>
                    <Title
                        title={currentTitle}
                        className={
                            classNames(
                                'n2o-sidebar__item-title',
                                { visible: isStaticView ? true : showContent },
                            )
                        }
                    />
                </Badge>
            </NavLink>
        </ExtendedTooltipComponent>
    )
}

InnerLink.propTypes = linkTypes
