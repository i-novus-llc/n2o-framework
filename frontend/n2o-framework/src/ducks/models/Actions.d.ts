import { Action } from '../Action'
import type { FormModelPrefix, ModelPrefix } from '../../core/models/types'

import { State } from './Models'

export interface ModelsPayload<T extends ModelPrefix = ModelPrefix> {
    prefix: T
    key: string
}

export type FieldPath = ModelsPayload & {
    field?: string
}

export type SetModelAction<Prefix extends ModelPrefix = ModelPrefix> = Action<string, ModelsPayload & {
    model: (Prefix extends (ModelPrefix.source | ModelPrefix.selected)
        ? Array<Record<string, unknown>>
        : Record<string, unknown>) | null
    isDefault?: boolean
}>

export type RemoveModelAction = Action<string, ModelsPayload>

export type SyncModelAction = Action<string, {
    prefix: ModelPrefix
    keys: string[]
    model: object
}>

export type UpdateModelAction = Action<
    string,
    ModelsPayload<FormModelPrefix> & {
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

export type AppendToArrayAction = Action<string, FieldPath & {
    primaryKey?: string
    value?: object
    position?: number
}>

export type RemoveFromArrayAction = Action<string, FieldPath & {
    start: number
    count?: number
}>

export type CopyFieldArrayAction = Action<string, FieldPath & {
    index: number
    primaryKey?: string
}>
