import React from 'react'
import classNames from 'classnames'

import { id as generateId } from '../../utils/id'
import { NavItemImage } from '../../components/snippets/NavItemImage/NavItemImage'
import { SimpleTooltip } from '../../components/snippets/Tooltip/SimpleTooltip'
import { renderBadge } from '../../components/snippets/Badge/Badge'

import { getCurrentTitle, Icon, Title } from './utils'
import { linkTypes } from './LinkTypes'

export function OuterLink({ href, title, icon, imageSrc, imageShape, type, sidebarOpen, isMiniView, item }) {
    const id = generateId()
    const currentTitle = getCurrentTitle(isMiniView, icon, title, imageSrc)

    return (
        <a id={id} className="n2o-sidebar__item" href={href}>
            {!imageSrc && <Icon icon={icon} title={title} type={type} sidebarOpen={sidebarOpen} />}
            <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
            <Title
                title={currentTitle}
                className={
                    classNames(
                        'n2o-sidebar__item__title',
                        { none: isMiniView && icon },
                    )
                }
            />
            {isMiniView && <SimpleTooltip id={id} message={title} placement="right" />}
            {renderBadge(item)}
        </a>
    )
}

OuterLink.propTypes = linkTypes
