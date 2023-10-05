/* eslint-disable @typescript-eslint/no-explicit-any */
import { FC, TdHTMLAttributes, VFC } from 'react'

import { Selection } from '../enum'

export type Cell = {
    id: string
    fieldId: string
    component: VFC<any>
    elementAttributes?: Partial<TdHTMLAttributes<HTMLTableCellElement>>
    treeExpandedCell?: boolean
    selection: Selection
}

export type HeaderCell = {
    disabled?: boolean
    visible?: boolean
    conditions?: Record<string, any>
    label: string
    multiHeader?: boolean;
    id: string
    fieldId: string
    elementAttributes: {
        alignment?: 'center' | 'right' | 'left' | 'justify'
        className?: string
    }
    component: FC
    children?: HeaderCell[]
    colSpan?: number
    rowSpan?: number
    hasSubColumns?: boolean
    sortingParam?: string
    filterControl?: {
        src: string
        id: string
        dateFormat?: string
        utc?: boolean,
        outputFormat?: string
        component: JSX.Element
    }
}
