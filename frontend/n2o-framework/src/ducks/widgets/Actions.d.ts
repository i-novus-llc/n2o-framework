import { Action } from '../Action'
import { Table } from '../table/Table'

import { Widget } from './Widgets'

export type RegisterPayload = {
    widgetId: string
    initProps: { type: string, table?: Table, datasource: string, saveSettings?: boolean }
    preInit: boolean
}

export type ResolvePayload = {
    widgetId: string
    modelId: string
    model: Record<string, unknown>
}

export type TogglePayload = {
    widgetId: string
    id?: string
    savedProps?: Widget | null
}

export type ChangeFilterVisibilityPayload = {
    widgetId: string
    isFilterVisible: boolean
}

export type Register = Action<string, RegisterPayload>
export type Resolve = Action<string, ResolvePayload>
export type Toggle = Action<string, TogglePayload>
export type ChangeFilterVisibility = Action<string, ChangeFilterVisibilityPayload>
