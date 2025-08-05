import { type MouseEvent } from 'react'

import { type HeaderCell, MOVE_MODE } from '../../../ducks/table/Table'

export interface ToggleColumnProps {
    id: string
    icon?: string
    label?: string
    entityKey: string
    nested: string
}

export interface DragHandleProps {
    label?: string
    className?: string
    empty?: boolean
}

export interface DropdownItemProps {
    item: HeaderCell
    onClick?(columnId: HeaderCell['columnId'], visibleState: HeaderCell['visibleState'], parentId?: HeaderCell['parentId']): void
    onMouseDown?(event: MouseEvent): void
    elementAttributes?: Record<string, unknown>
}

interface Enhancer {
    items: Array<DropdownItemProps['item']>
    onClick: DropdownItemProps['onClick']
}

export interface DragAndDropMenuProps extends Enhancer {
    id: string
    moveMode?: MOVE_MODE
    ghostContainerId: string
}

export interface MultiColumnLabelProps {
    label?: string
    level: number
    checked: boolean
    enabled: boolean
    indeterminate: boolean
    open: boolean
    onClick?(): void
    onToggleClick?(e: MouseEvent): void
}

export interface MultiColumnProps extends Enhancer {
    className?: string
    label?: string
    level?: number
    enabled: boolean
}

export interface ToggleColumnCheckboxProps {
    className?: string
    label?: string
    checked: boolean
    indeterminate?: boolean
    enabled?: boolean
}
