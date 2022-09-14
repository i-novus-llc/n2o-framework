import React from 'react'
import classNames from 'classnames'

import { id as generateId } from '../../utils/id'
import { NavItemImage } from '../../components/snippets/NavItemImage/NavItemImage'
import { Badge } from '../../components/snippets/Badge/Badge'
import { ExtendedTooltipComponent } from '../../components/snippets/Tooltip/TooltipHOC'

import { getCurrentTitle, Icon, Title } from './utils'
import { linkTypes } from './LinkTypes'

export function OuterLink({ href, title, icon, imageSrc, imageShape, type, sidebarOpen, isMiniView, item }) {
    const id = generateId()
    const currentTitle = getCurrentTitle(isMiniView, icon, title, imageSrc)

    const hint = isMiniView ? title : null

    return (
        <ExtendedTooltipComponent hint={hint} placement="right">
            <a id={id} className="n2o-sidebar__item" href={href}>
                {!imageSrc && <Icon icon={icon} title={title} type={type} sidebarOpen={sidebarOpen} />}
                <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
                <Badge {...item.badge}>
                    <Title
                        title={currentTitle}
                        className={
                            classNames(
                                'n2o-sidebar__item__title',
                                { none: isMiniView && icon },
                            )
                        }
                    />
                </Badge>
            </a>
        </ExtendedTooltipComponent>
    )
}

OuterLink.propTypes = linkTypes
