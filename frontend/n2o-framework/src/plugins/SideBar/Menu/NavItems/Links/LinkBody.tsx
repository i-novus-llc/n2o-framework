import React from 'react'
import classNames from 'classnames'
import { NavItemImage } from '@i-novus/n2o-components/lib/display/NavItemImage'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import { Badge } from '../../../../../components/snippets/Badge/Badge'
import { Item } from '../../../../CommonMenuTypes'
import { Icon, getCurrentTitle, needRender } from '../../../utils'

export interface ItemContent extends Item {
    sidebarOpen: boolean
    isStaticView: boolean
    showContent: boolean
    isMiniView: boolean
}

export function LinkBody({
    icon,
    title,
    src,
    sidebarOpen,
    imageSrc,
    imageShape,
    badge,
    isStaticView,
    showContent,
    isMiniView,
    className,
    style,
}: ItemContent) {
    const currentTitle = getCurrentTitle(isMiniView, icon, title, imageSrc)

    return (
        <>
            <Icon icon={icon} title={title || ''} src={src} sidebarOpen={sidebarOpen} />
            <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
            <Badge {...badge}>
                {needRender(currentTitle) && (
                    <span
                        style={style}
                        className={classNames(
                            'n2o-sidebar__item-title',
                            className,
                            { visible: isStaticView ? true : showContent },
                        )}
                    >
                        <Text>{currentTitle}</Text>
                    </span>
                )}
            </Badge>
        </>
    )
}
