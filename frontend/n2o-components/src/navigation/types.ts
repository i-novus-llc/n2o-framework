import { CSSProperties, ReactNode } from 'react'

export enum Direction {
    ROW = 'row',
    COLUMN = 'column',
}

export enum Position {
    LEFT = 'left',
    RIGHT = 'right',
}

export enum LinkTarget {
    BLANK = '_blank',
    SELF = '_self',
    PARENT = '_parent',
    TOP = '_top',
}

export enum GroupView {
    EXPANDED = 'expanded',
    COLLAPSED = 'collapsed',
}

export enum Trigger {
    HOVER = 'hover',
    CLICK = 'click',
}

export interface CommonProps {
    label?: string
    className?: string
    style?: CSSProperties
    visible?: boolean
    enabled?: boolean
    icon?: string
    iconPosition?: Position
    children?: ReactNode
    rootClassName?: string
    // className каждого родительского тега элемента Panel
    childrenClassName?: string
}

export interface PanelProps extends CommonProps {
    direction?: Direction
}

export interface LinkProps extends CommonProps {
    url?: string
    target: LinkTarget
}

export interface GroupProps extends CommonProps {
    collapsible?: boolean
    defaultState?: GroupView
}

export interface DropdownProps {
    label: string
    trigger?: Trigger
    position?: Position
    visible?: boolean
    enabled?: boolean
    className?: string
    rootClassName?: string
    style?: CSSProperties
    children?: ReactNode
}
