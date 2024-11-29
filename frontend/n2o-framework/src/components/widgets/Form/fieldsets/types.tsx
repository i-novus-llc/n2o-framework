import { CSSProperties, ReactNode } from 'react'
import { Props as BadgeProps } from '@i-novus/n2o-components/lib/display/Badge/Badge'

export type Rows = string[]
export type Render = (rows: Rows) => ReactNode

export interface FieldsetProps {
    className?: string
    style?: CSSProperties
    needLabel?: boolean
    needDescription?: boolean
    description: string
    label: string
    help: string
    type: string
    childrenLabel?: string
    enabled?: boolean
    activeModel?: Record<string, unknown>
    render: Render
    visible?: boolean
    badge: BadgeProps
    rows: Rows
    disabled: boolean
    showLine?: boolean
    subTitle?: string
    expand?: boolean
    hasArrow?: boolean
    hasSeparator?: boolean
    collapsible?: boolean
}
