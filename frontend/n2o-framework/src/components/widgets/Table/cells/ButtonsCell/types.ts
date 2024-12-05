import { ReactNode } from 'react'
import { Action } from 'redux'
import { type PopperProps } from 'react-popper'

import { ToolbarProps } from '../../../../buttons/Toolbar'

type Menu = Array<Record<string, unknown>>

export interface MenuItem {
    title: string
    visible: boolean
    icon: string
    action: Action
    color: string
}

export type HintButtonProps = {
    title?: string
    hint?: string
    visible?: boolean
    uId: string
    icon?: string
    onClick(event: MouseEvent, action: Action): void
    action: Action
    hintPosition?: string
    size?: string
    active?: boolean
    color?: string
    disabled?: boolean
    delay?: number | { show: number, hide: number }
    hideArrow?: boolean
    offset?: string | number
}

export interface HintDropdownBodyProps extends Omit<PopperProps<unknown>, 'children'> {
    positionFixed: boolean
    menu: Menu
    createDropDownMenu(item: MenuItem): ReactNode
    open: boolean
    onToggleDropdown(): void
}

export interface HintDropdownProps extends Pick<PopperProps<unknown>, 'modifiers' | 'placement'> {
    uId: string
    title: string
    hint: string
    visible: boolean
    menu: Menu
    icon: string
    onClick(event: MouseEvent, action: Action): void
    hintPosition: string
    positionFixed: boolean
    resolveWidget(model: Record<string, unknown>): void
    model: Record<string, unknown>
    direction: string
    active: boolean
    disabled: boolean
    size: string
    color: string
    delay?: number | { show: number, hide: number }
    hideArrow: boolean
    offset?: string | number
}

export interface ButtonCellProps extends Pick<PopperProps<unknown>, 'placement'> {
    id: string
    className: string
    model: Record<string, unknown>
    toolbar: ToolbarProps
    onResolve(): void
    tooltipFieldId: string
    visible: boolean
    disabled: boolean
    isControlledTooltip: boolean
    callAction(model: Record<string, unknown>): void
}
