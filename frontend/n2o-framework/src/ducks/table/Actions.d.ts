import { Action } from '../Action'

import { Table } from './Table'

export type RegisterTablePayload = {
    widgetId: string
    initProps: Table
}

export type RegisterTableColumnPayload = {
    widgetId: string
    columnId: string
    label: string
    visible: boolean
    disabled: boolean
    conditions: Record<string, unknown>
}

export type ChangeTableColumnParamPayload = {
    widgetId: string
    columnId: string
    paramKey: string
    value: string
}

export type ChangeTableParamPayload = {
    widgetId: string
    paramKey: string
    value: string
}

export type SwitchTableColumnParamPayload = {
    widgetId: string
    paramKey: string
}

export type RegisterTable = Action<string, RegisterTablePayload>
export type RegisterTableColumn = Action<string, RegisterTableColumnPayload>
export type ChangeTableColumnParam = Action<string, ChangeTableColumnParamPayload>
export type ChangeTableParam = Action<string, ChangeTableParamPayload>
export type SwitchTableColumnParam= Action<string, SwitchTableColumnParamPayload>
