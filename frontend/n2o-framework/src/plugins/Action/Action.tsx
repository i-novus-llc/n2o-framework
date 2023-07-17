import React, { useCallback } from 'react'
import { useDispatch } from 'react-redux'
import classNames from 'classnames'

import { ExtendedTooltipComponent } from '../../components/snippets/Tooltip/TooltipHOC'
import { LinkBody as SidebarItem } from '../SideBar/Menu/NavItems/Links/LinkBody'
import { LinkBody as HeaderItem } from '../Header/SimpleHeader/Menu/NavItems/Links/LinkBody'
import { IItem } from '../CommonMenuTypes'

interface IAction {
    item: IItem,
    className: string
    isStaticView: boolean
    sidebarOpen: boolean
    showContent: boolean
    isMiniView: boolean
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
