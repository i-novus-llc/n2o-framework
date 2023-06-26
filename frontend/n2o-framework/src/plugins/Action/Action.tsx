import React, { useCallback } from 'react'
import { useDispatch } from 'react-redux'
import classNames from 'classnames'

import { ExtendedTooltipComponent } from '../../components/snippets/Tooltip/TooltipHOC'
import { ItemContent as SidebarItem, IItemContent } from '../SideBar/ItemContent'
import { LinkBody as HeaderItem } from '../Header/SimpleHeader/NavItems/Links/LinkBody'

interface IAction {
    item: IItemContent,
    className?: string
    isStaticView?: boolean
    sidebarOpen?: boolean
    showContent?: boolean
    isMiniView?: boolean
    from: 'HEADER' | 'SIDEBAR'
}

export function Action(props: IAction) {
    const {
        item,
        className,
        sidebarOpen,
        isStaticView,
        showContent,
        isMiniView,
        from = 'SIDEBAR',
    } = props
    const { id, title, action } = item

    const dispatch = useDispatch()
    const onClick = useCallback(() => dispatch(action), [action, dispatch])

    const hint = isMiniView ? title : null

    return (
        <ExtendedTooltipComponent
            hint={hint}
            placement="right"
        >
            <div
                id={id}
                className={classNames('n2o-action-item', className)}
                onClick={onClick}
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
            </div>
        </ExtendedTooltipComponent>
    )
}
