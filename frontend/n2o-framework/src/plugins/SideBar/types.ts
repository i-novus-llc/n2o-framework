import { type LogoProps } from '../Header/SimpleHeader/Logo'
import { Item } from '../CommonMenuTypes'

export const SIDEBAR_VIEW = {
    none: 'none',
    micro: 'micro',
    mini: 'mini',
    maxi: 'maxi',
}

export type SidebarView = keyof typeof SIDEBAR_VIEW

export interface LogoSectionProps {
    isMiniView: boolean
    logo: LogoProps
    subtitle?: string
    showContent: boolean
}

type onMouseEvent = () => void

export interface SidebarProps extends Pick<LogoSectionProps, 'logo' | 'subtitle'> {
    activeId: string
    sidebarOpen: boolean
    className?: string
    onMouseEnter: onMouseEvent
    onMouseLeave: onMouseEvent
    isStaticView: boolean
    datasource: string
    visible: boolean
    side: 'left' | 'right'
    menu: { items: Item[] }
    extraMenu: Item[]
    controlled: boolean
    defaultState: SidebarView
    toggledState: SidebarView
    force?: boolean
    fetch?: string
    path?: string
    id?: string
}

export interface SimpleSidebarProps extends Omit<SidebarProps, 'isStaticView' | 'onMouseEnter' | 'onMouseLeave'> {
    onMouseEnter: boolean | onMouseEvent
    onMouseLeave: boolean | onMouseEvent
}
