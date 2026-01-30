import React from 'react'
import { Link as LinkComponent } from '@i-novus/n2o-components/lib/navigation/Link'
import classNames from 'classnames'

import { Item } from '../../../../CommonMenuTypes'
import { Tooltip } from '../../../../../components/snippets/Tooltip/TooltipHOC'
import { useLink } from '../../../../../components/core/router/useLink'

import { LinkBody } from './LinkBody'

interface LinkProps {
    item: Item
    sidebarOpen: boolean
    isMiniView: boolean
    isStaticView: boolean
    showContent: boolean
}

export function Link({ item, sidebarOpen, isMiniView, isStaticView, showContent }: LinkProps) {
    const { href, title, disabled, target } = item

    const hint = isMiniView ? title : null
    const { active, ...linkProps } = useLink({ href, disabled, target })

    return (
        <Tooltip placement="right" hint={hint}>
            <LinkComponent
                className={classNames('n2o-sidebar__item', { active })}
                disabled={disabled}
                {...linkProps}
                label={(
                    <LinkBody
                        {...item}
                        sidebarOpen={sidebarOpen}
                        isStaticView={isStaticView}
                        showContent={showContent}
                        isMiniView={isMiniView}
                    />
                )}
            />
        </Tooltip>
    )
}

Link.displayName = 'n2o/Sidebar/Link'
