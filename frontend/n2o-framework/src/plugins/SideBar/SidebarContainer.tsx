import React from 'react'

import { SideBar } from './SideBar'
import { type SimpleSidebarProps } from './types'

/**
 * Компонент контейнер для {@link SideBar}
 */
export function SimpleSidebar(props: SimpleSidebarProps) {
    const { defaultState, toggledState, controlled, onMouseEnter, onMouseLeave } = props

    const isStaticView = defaultState === toggledState

    const handleMouseEnter = () => {
        if (!isStaticView && controlled && typeof onMouseEnter === 'function') { onMouseEnter() }
    }

    const handleMouseLeave = () => {
        if (!isStaticView && controlled && typeof onMouseLeave === 'function') { onMouseLeave() }
    }

    return (
        <SideBar
            {...props}
            isStaticView={isStaticView}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
        />
    )
}

export default SimpleSidebar
