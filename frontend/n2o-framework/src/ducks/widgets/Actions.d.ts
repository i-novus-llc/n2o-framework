import { Action } from '../Action'
import { Table } from '../table/Table'

export type RegisterPayload = {
    widgetId: string
    initProps: { type: string, table?: Table }
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
}

export type ChangeFilterVisibilityPayload = {
    widgetId: string
    isFilterVisible: boolean
}

export type Register = Action<string, RegisterPayload>
export type Resolve = Action<string, ResolvePayload>
export type Toggle = Action<string, TogglePayload>
export type ChangeFilterVisibility = Action<string, ChangeFilterVisibilityPayload>
