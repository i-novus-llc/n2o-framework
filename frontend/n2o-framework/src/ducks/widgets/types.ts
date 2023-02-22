import { ModelPrefix } from '../../core/datasource/const'

export type TWidgetState = Record<string, TWidget>

// TODO: Дописать тип
export type TWidget = {
    isInit: boolean
    visible: boolean
    fetch: string
    disabled: boolean
    isResolved: boolean
    isFilterVisible: boolean
    isActive: boolean
    pageId: string
    error: unknown
    validation: Record<string, unknown>
    id: string
    name: string
    datasource: string
    fetchOnInit: boolean
    className: string
    children: string
    modelId?: string
    type?: string
    dataProvider?: unknown
    toolbar: Record<string, unknown>
    paging: Record<string, unknown>
    filter: Record<string, unknown>
    table: Record<string, unknown>
    form?: {
        modelPrefix: ModelPrefix
        [key: string]: unknown
    }
}
