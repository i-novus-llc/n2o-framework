import { Action } from '../Action'

import { Table } from './Table'

export type RegisterTablePayload = {
    widgetId: string
    initProps: Table
}

export type ChangeTableColumnParamPayload = {
    widgetId: string
    id: string
    paramKey: string
    value: string | boolean | number
    parentId?: string
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

export type ReorderTableColumnPayload = {
    widgetId: string
    headerId: string
    reorderColumnId: string
    targetColumnId: string
}

export type UpdateTableParamsPayload = {
    widgetId: string
    params: Table
}

export type RegisterTable = Action<string, RegisterTablePayload>
export type ChangeTableColumnParam = Action<string, ChangeTableColumnParamPayload>
export type ChangeTableParam = Action<string, ChangeTableParamPayload>
export type SwitchTableColumnParam = Action<string, SwitchTableColumnParamPayload>
export type ReorderTableColumn = Action<string, ReorderTableColumnPayload>
export type UpdateTableParams = Action<string, UpdateTableParamsPayload>
