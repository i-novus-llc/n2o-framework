import { CSSProperties, ReactNode, Ref } from 'react'
import { CardProps } from 'reactstrap/es/Card'

export enum PANEL_COLORS {
    DEFAULT = 'secondary',
    PRIMARY = 'primary',
    SUCCESS = 'success',
    INFO = 'info',
    WARNING = 'warning',
    DANGER = 'danger',
}

export interface CommonProps {
    children?: ReactNode
    id?: string
    className?: string
    style?: CSSProperties
}

export interface PanelBodyProps extends CommonProps {
    activeKey?: string
    hasTabs: boolean
}

export interface PanelTitleProps extends CommonProps {
    icon: string
    toggleCollapse(): void
}

export interface PanelNavItemProps extends CommonProps {
    onClick(event: Event, id?: string): void
    isToolBar?: boolean
    active?: boolean
    disabled?: boolean
}

export interface PanelTabBodyProps extends CommonProps {
    eventKey: string | number,
}

export type TabNavProps = CommonProps

export interface PanelMenuProps extends CommonProps {
    onToggle(): void
    onFullScreenClick(): void
    isOpen: boolean
    fullScreenIcon: string
    fullScreen: boolean
    collapsible: boolean
}

export interface CommonPanelProps {
    onToggle?(): void
    disabled: boolean
    t?(text: string): string
    isFullScreen: boolean
    color: PANEL_COLORS
}

export type PanelProps = CardProps & CommonPanelProps

export interface PanelTab extends CommonProps {
    disabled: boolean
    header: boolean
    content: ReactNode
    id: string
    src?: string
    fetchOnInit?: boolean
    widgetId: string
    label: boolean
}

interface PanelToolbar extends PanelTab {
    onClick(event: Event, id?: string): void
}

type Enhancer = CommonPanelProps & CommonProps
export interface PanelContainerProps extends Enhancer {
    toolbar: PanelToolbar[]
    icon: string
    headerTitle: string
    footerTitle: string
    innerRef: Ref<HTMLElement>
    onVisibilityChange(value: boolean): void
    onKeyPress(value: boolean): void
    tabs: PanelTab[]
    open: boolean
    collapsible: boolean
    hasTabs: boolean
    fullScreen: boolean
    header: boolean
}
