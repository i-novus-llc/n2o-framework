import { PayloadAction } from '@reduxjs/toolkit'

import type { ModelPrefix } from '../../core/datasource/const'

export interface ModelsPayload {
    prefix: ModelPrefix
    key: string
}

type FieldPath = ModelsPayload & {
    field: string
}

export type SetModelAction = PayloadAction<ModelsPayload & {
    model?: object | null
    isDefault?: boolean
}>

export type RemoveModelAction = PayloadAction<ModelsPayload>

export type SyncModelAction = PayloadAction<{
    prefix: ModelPrefix
    keys: string[]
    model: object
}>

export type UpdateModelAction = PayloadAction<ModelsPayload & {
    field: string
    value: unknown
}>

export type RemoveAllModelAction = PayloadAction<{
    key: string
}>

export type ClearModelAction = PayloadAction<{
    key: string
    prefixes: ModelPrefix[]
}>

export type MergeModelAction = PayloadAction<{
    combine: Record<ModelPrefix, Record<string, object | object[]>>
}>

export type CopyAction = PayloadAction<{
    source: FieldPath
    target: FieldPath
    mode: 'replace' | 'merge' | 'add'
    sourceMapper?: string
}>

export type AppendFieldToArrayAction = PayloadAction<FieldPath & {
    primaryKey?: string
    value?: object
}>

export type RemoveFieldFromArrayAction = PayloadAction<FieldPath & {
    start: number
    end?: number
}>

export type CopyFieldArrayAction = PayloadAction<FieldPath & {
    index: number
    primaryKey?: string
}>
