/* eslint-disable @typescript-eslint/no-explicit-any */
import { FC, TdHTMLAttributes, VFC } from 'react'

import { SelectionType } from '../enum'

export type Cell = {
    id: string
    fieldId: string
    component: VFC<any>
    elementAttributes?: Partial<TdHTMLAttributes<HTMLTableCellElement>>
    treeExpandedCell?: boolean
    selection: SelectionType
}

export type HeaderCell = {
    disabled?: boolean
    visible?: boolean
    conditions?: object
    label: string
    multiHeader?: boolean;
    id: string
    fieldId: string
    elementAttributes: {
        alignment?: 'center' | 'right' | 'left' | 'justify'
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
