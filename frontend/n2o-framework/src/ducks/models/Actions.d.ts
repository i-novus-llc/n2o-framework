import type { ModelPrefix } from '../../core/datasource/const'

export interface ModelsPayload {
    prefix: ModelPrefix
    key: string
}

export interface ModelsAction<
    TPayload extends object,
    TMeta extends object = object
> {
    type: string
    payload: TPayload
    meta?: TMeta
}

export type SetModelAction = ModelsAction<ModelsPayload & {
    model: object
}>

export type RemoveModelAction = ModelsAction<ModelsPayload>

export type SyncModelAction = ModelsAction<{
    prefix: ModelPrefix
    keys: string[]
    model: object
}>

export type UpdateModelAction = ModelsAction<ModelsPayload & {
    field: string
    value: unknown
}>

export type RemoveAllModelAction = ModelsAction<{
    key: string
}>

export type ClearModelAction = ModelsAction<{
    key: string
    prefixes: ModelPrefix[]
}>

export type MergeModelAction = ModelsAction<{
    combine: unknown
}>

export type CopyAction = ModelsAction<{
    source: ModelsPayload
    target: ModelsPayload
    mode: 'replace' | 'merge' | 'add'
    sourceMapper?: string
}>
