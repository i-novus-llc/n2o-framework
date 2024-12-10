import { LegacyRef } from 'react'

interface Item {
    src: string
    component: string
    id?: string
    className?: string
    visible?: boolean
}

export interface SubMenuProps {
    items: Item[]
    actionCallback(): void
    entityKey: string
    toggle(): void
    onClick(): void
}

type EntityKey = string

export interface DropdownProps {
    subMenu: Item[]
    entityKey: EntityKey
    className?: string
    showToggleIcon: boolean
    tooltipTriggerRef: LegacyRef<HTMLElement>
    storeButtons: Record<string, { visible: boolean }>
    actionCallback(): void
    datasource: string
    placement: string
    hintPosition: string
    visible: boolean
    forwardedRef: LegacyRef<HTMLDivElement>
}

export interface DropdownComponentProps extends Omit<DropdownProps, 'storeButtons'> {
    visible: boolean
    toggle(): void
    onClick(): void
    popperKey: number
    open: boolean
    forwardedRef: LegacyRef<HTMLDivElement>
}
