/* eslint-disable @typescript-eslint/no-explicit-any */
import { ComponentType, CSSProperties, FC, VFC } from 'react'

import { Selection } from '../enum'

export type Cell = {
    id: string
    fieldId: string
    component: VFC<any>
    elementAttributes?: Record<string, any>
    treeExpandedCell?: boolean
    selection: Selection
}

export type HeaderCell = {
    disabled?: boolean
    visible?: boolean
    conditions?: Record<string, any>
    label: string
    icon?: string
    multiHeader?: boolean;
    id: string
    fieldId: string
    elementAttributes: {
        alignment?: 'center' | 'right' | 'left' | 'justify'
        className?: string
        width?: string
    }
    component: FC
    children?: HeaderCell[]
    colSpan?: number
    rowSpan?: number
    hasSubColumns?: boolean
    sortingParam?: string
    filterField?: {
        src: string
        id: string
        dateFormat?: string
        utc?: boolean,
        outputFormat?: string
        component: ComponentType<Record<string, unknown>>
        control?: Record<string, unknown>
        style?: CSSProperties
    }
}
