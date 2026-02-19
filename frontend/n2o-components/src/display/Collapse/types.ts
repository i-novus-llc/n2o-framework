import React, { ReactNode } from 'react'

export interface useCollapseProps {
    collapsible?: boolean
    defaultActiveKey?: string | string[] | null
    activeKey?: string | string[]
    accordion?: boolean
    onChange?(activeKey: string | string[]): void
}

export interface CollapseProps extends useCollapseProps {
    className?: string
    children: ReactNode
    destroyInactivePanel?: boolean
    isVisible?: boolean
    expandIcon?(isActive: boolean, collapsible?: boolean): ReactNode
    style?: React.CSSProperties
}

export interface CollapseContextType {
    activeKeys: string[]
    togglePanel(key: string): void
    collapsible?: boolean
    destroyInactivePanel?: boolean
    accordion?: boolean
    expandIcon?(isActive: boolean, collapsible?: boolean): ReactNode
}
