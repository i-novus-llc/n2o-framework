import { CSSProperties, ReactNode, SyntheticEvent } from 'react'

export enum Direction {
    ROW = 'row',
    COLUMN = 'column',
}

export enum Position {
    LEFT = 'left',
    RIGHT = 'right',
}

export type LinkTarget = '_blank' |  '_self' | '_parent' | '_top'

export enum GroupView {
    EXPANDED = 'expanded',
    COLLAPSED = 'collapsed',
}

export enum Trigger {
    HOVER = 'hover',
    CLICK = 'click',
}

export interface CommonProps {
    label?: string | ReactNode
    className?: string
    style?: CSSProperties
    disabled?: boolean
    icon?: string
    iconPosition?: Position
    children?: ReactNode
}

export interface PanelProps extends CommonProps {
    direction?: Direction
}

export interface LinkProps extends CommonProps {
    url?: string
    target?: LinkTarget
    onClick?(e: SyntheticEvent): void
}

export interface GroupProps extends CommonProps {
    collapsible?: boolean
    defaultState?: GroupView
}

export interface DropdownProps {
    label: string | ReactNode
    trigger?: Trigger
    position?: Position
    disabled?: boolean
    className?: string
    style?: CSSProperties
    children?: ReactNode
}
