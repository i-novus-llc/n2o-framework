import { Action } from '../Action'

export type RegisterPayload = {
    widgetId: string
    initProps: { type: string }
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
