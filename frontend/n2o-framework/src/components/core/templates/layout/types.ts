import { ReactNode } from 'react'

export interface LayoutProps {
    className: string
    header: ReactNode
    side: 'left' | 'right'
    children: ReactNode
    fixed: boolean
    sidebar: ReactNode
    footer: ReactNode
}
