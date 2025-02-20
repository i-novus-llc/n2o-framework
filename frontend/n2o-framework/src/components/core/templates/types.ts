import { ReactNode } from 'react'

import { type SidebarProps } from '../../../plugins/SideBar/types'

export interface TemplateProps {
    children: ReactNode
}

export interface SidebarTemplateProps extends SidebarProps {
    overlay?: boolean
    toggleOnHover: boolean
    openSideBar: SidebarProps['onMouseEnter']
    closeSideBar: SidebarProps['onMouseLeave']
    fullSizeHeader: boolean
    fixed: boolean
}
