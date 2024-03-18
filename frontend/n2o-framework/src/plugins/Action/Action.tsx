import React, { useCallback } from 'react'
import classNames from 'classnames'

import { useDispatch } from '../../core/Redux/useDispatch'
import { Tooltip } from '../../components/snippets/Tooltip/TooltipHOC'
import { LinkBody as SidebarItem } from '../SideBar/Menu/NavItems/Links/LinkBody'
import { LinkBody as HeaderItem } from '../Header/SimpleHeader/Menu/NavItems/Links/LinkBody'
import { Item } from '../CommonMenuTypes'

interface ActionProps {
    item: Item,
    className: string
    isStaticView: boolean
    sidebarOpen: boolean
    showContent: boolean
    isMiniView: boolean
    from: 'HEADER' | 'SIDEBAR'
}

export function Action(props: ActionProps) {
    const {
        item,
        className,
        sidebarOpen,
        isStaticView,
        showContent,
        isMiniView,
        from = 'SIDEBAR',
    } = props
    const { id, title, action, style } = item

    const dispatch = useDispatch()
    const onClick = useCallback(() => dispatch(action), [action, dispatch])

    const hint = isMiniView ? title : null

    return (
        <Tooltip
            hint={hint}
            placement="right"
        >
            {/* eslint-disable-next-line jsx-a11y/no-noninteractive-element-interactions */}
            <li
                id={id}
                className={classNames('n2o-action-item', className)}
                onClick={onClick}
                style={style}
            >
                {from === 'SIDEBAR'
                    ? (
                        <SidebarItem
                            {...item}
                            sidebarOpen={sidebarOpen}
                            isStaticView={isStaticView}
                            showContent={showContent}
                            isMiniView={isMiniView}
                        />
                    )
                    : <HeaderItem {...item} />}
            </li>
        </Tooltip>
    )
}
