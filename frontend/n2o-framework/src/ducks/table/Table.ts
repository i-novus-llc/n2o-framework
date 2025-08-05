import { ComponentType, CSSProperties, FC, MouseEvent, VFC } from 'react'

import { type Condition } from '../toolbar/Toolbar'
import { Selection } from '../../components/Table'

export enum MOVE_MODE {
    TABLE = 'table',
    SETTINGS = 'settings',
    ALL = 'all',
}

export type FilterField = {
    src: string
    id: string
    dateFormat?: string
    utc?: boolean,
    outputFormat?: string
    component: ComponentType<Record<string, unknown>>
    control?: Record<string, unknown>
    style?: CSSProperties
}

export interface BodyCell {
    id: string
    label: string
    src: string
    fieldId: string
    component: VFC<Record<string, unknown>>
    elementAttributes?: Record<string, unknown>
    treeExpandedCell?: boolean
    selection: Selection
    children?: HeaderCell['children']
    visibleState?: HeaderCell['visibleState']
    format?: string
    filterField?: FilterField
}

export type HeaderCell = {
    parentId?: string
    moveMode?: MOVE_MODE
    enabled: boolean
    visible: boolean
    columnId: string
    visibleState: boolean
    conditions?: Condition
    label: string
    icon?: string
    multiHeader?: boolean;
    id: string
    fieldId: string
    elementAttributes: {
        alignment?: 'center' | 'right' | 'left' | 'justify'
        className?: string
        width?: string
        style?: CSSProperties
        onMouseDown?(event: MouseEvent): void
        id?: string
        'data-draggable'?: boolean
        // className for a LeafComponent
        componentClassName?: string
    }
    component: FC
    children?: HeaderCell[]
    colSpan?: number
    rowSpan?: number
    hasSubColumns?: boolean
    sortingParam?: string
    filterField?: FilterField
    format?: string
}

export interface Table {
    textWrap?: boolean
    header?: {
        cells: HeaderCell[]
    }
    body?: {
        cells: BodyCell[]
    }
    saveSettings?: boolean
    defaultProps?: Table
    datasource?: string
}

export type State = Record<string, Table>
