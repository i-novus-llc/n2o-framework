import { Action } from '../Action'
import type { ModelPrefix } from '../../core/datasource/const'

import { State } from './Models'

export interface ModelsPayload {
    prefix: ModelPrefix
    key: string
}

type FieldPath = ModelsPayload & {
    field: string
}

export type SetModelAction<Prefix extends ModelPrefix = ModelPrefix.active> = Action<string, ModelsPayload & {
    model: Prefix extends (ModelPrefix.source | ModelPrefix.selected)
        ? Array<Record<string, unknown>>
        : Record<string, unknown>
    isDefault?: boolean
}>

export type RemoveModelAction = Action<string, ModelsPayload>

export type SyncModelAction = Action<string, {
    prefix: ModelPrefix
    keys: string[]
    model: object
}>

export type UpdateModelAction = Action<string, ModelsPayload & {
    field: string
    value: unknown
}>

export type RemoveAllModelAction = Action<string, {
    key: string
}>

export type ClearModelAction = Action<string, {
    key: string
    prefixes: ModelPrefix[]
}>

export type MergeModelAction = Action<string, {
    combine: Partial<State>
}>

export type CopyAction = Action<string, {
    source: FieldPath
    target: FieldPath
    mode: 'replace' | 'merge' | 'add'
    sourceMapper?: string
}>

export type AppendFieldToArrayAction = Action<string, FieldPath & {
    primaryKey?: string
    value?: object
}>

export type RemoveFieldFromArrayAction = Action<string, FieldPath & {
    start: number
    end?: number
}>

export type CopyFieldArrayAction = Action<string, FieldPath & {
    index: number
    primaryKey?: string
}>
