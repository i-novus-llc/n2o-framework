import { ModelPrefix } from '../../core/datasource/const'

export type Widget = {
    isInit: boolean
    visible: boolean
    fetch: string
    disabled: boolean
    isResolved: boolean
    isFilterVisible: boolean
    isActive: boolean
    pageId: string | null
    error: unknown
    validation?: Record<string, unknown>
    id?: string
    name?: string
    datasource?: string
    fetchOnInit?: boolean
    className?: string
    children?: string
    modelId?: string
    type?: string | null
    dataProvider?: unknown
    toolbar?: Record<string, unknown>
    paging?: Record<string, unknown>
    filter?: Record<string, unknown>
    table?: { textWrap: boolean, columns: Record<string, { visible: boolean }> }
    form?: {
        modelPrefix: ModelPrefix
        [key: string]: unknown
    }
    fetchOnVisibility?: boolean
    dependency?: Record<string, unknown>
    parent?: string
}

export type State = Record<string, Widget>
