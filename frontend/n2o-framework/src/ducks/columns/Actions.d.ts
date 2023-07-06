import { Action } from '../Action'

export type RegisterColumnPayload = {
    key: string
    columnId: string
    label: string
    visible: boolean
    disabled: boolean
    conditions: object
}

export type ChangeColumnVisibilityPayload = {
    columnId: string
    visible: boolean
    key: string
    isInit?: boolean
    disabled?: boolean
    frozen?: boolean
}

export type ChangeColumnDisabledPayload = {
    widgetId?: string
    columnId: string
    disabled: boolean
    key: string
}

export type ToggleColumnVisibilityPayload = {
    widgetId?: string
    columnId: string
    key: string
}

export type ChangeFrozenColumnPayload = {
    widgetId?: string
    columnId: string
    key: string
}

export type RegisterColumn = Action<string, RegisterColumnPayload>
export type ChangeColumnVisibility = Action<string, ChangeColumnVisibilityPayload>
export type ChangeColumnDisabled = Action<string, ChangeColumnDisabledPayload>
export type ToggleColumnVisibility = Action<string, ToggleColumnVisibilityPayload>
export type ChangeFrozenColumn = Action<string, ChangeFrozenColumnPayload>
