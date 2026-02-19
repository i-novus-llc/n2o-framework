import { ReactNode, CSSProperties, RefObject } from 'react'

export interface PanelHeaderProps {
    header: ReactNode
    headerClass?: string
    showArrow?: boolean
    expandIcon?(isActive: boolean, collapsible?: boolean): ReactNode
    extra?: ReactNode
    hasSeparator?: boolean
    collapsible?: boolean
}

export interface PanelContentProps {
    children: ReactNode
    forceRender?: boolean
    destroyInactivePanel?: boolean
    openAnimation?: {
        duration?: number // ms
        easing?: string;  // пример: ease-in-out
    }
}

export interface HeaderProps extends PanelHeaderProps {
    isActive: boolean
    isDisabled: boolean
    onClick(): void
}

export interface ContentProps extends PanelContentProps {
    isActive: boolean
    openAnimation: NonNullable<PanelContentProps['openAnimation']>
    contentRef: RefObject<HTMLDivElement>
}

export interface PanelProps extends PanelHeaderProps, PanelContentProps {
    key?: string
    panelKey?: string
    className?: string
    style?: CSSProperties
    disabled?: boolean
}
