import { ReactNode } from 'react'

// TODO temporary ConfigContainer interface
export interface ConfigContainerProps {
    layout: never
    header?: {}
    sidebar?: {}
    footer?: {}
}

export interface TemplateProps {
    children: ReactNode
}
