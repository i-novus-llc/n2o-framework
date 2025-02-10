import { ReactNode } from 'react'
import { Dispatch } from 'redux'

import { ModelPrefix } from '../../core/datasource/const'
import type { Props as N2OPaginationProps } from '../../components/widgets/Table/N2OPagination'

export type Widget = {
    isInit?: boolean
    visible?: boolean
    fetch?: string
    disabled?: boolean
    isFilterVisible?: boolean
    isActive?: boolean
    pageId?: string | null
    error?: unknown
    validation?: Record<string, unknown>
    id?: string
    name?: string
    datasource?: string
    fetchOnInit?: boolean
    className?: string
    children?: ReactNode
    modelId?: string
    type?: string | null
    dataProvider?: unknown
    toolbar?: Record<string, unknown>
    paging?: { showCount: boolean, place: string } & N2OPaginationProps
    // TODO разобраться, filter в Tree (требуется рефакторинг) из TreeWidget type = string
    filter?: Record<string, unknown> | string
    table?: { textWrap: boolean, columns: Record<string, { visible: boolean }> }
    form?: {
        modelPrefix: ModelPrefix
        [key: string]: unknown
    }
    fetchOnVisibility?: boolean
    dependency?: Record<string, unknown>
    parent?: string
    dispatch?: Dispatch
}

export type State = Record<string, Widget>
