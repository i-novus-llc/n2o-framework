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
    model: object
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

export type UpdateMapModelAction = PayloadAction<ModelsPayload & {
    field: string
    value: unknown
    map: string
}>

export type RemoveAllModelAction = PayloadAction<{
    key: string
}>

export type ClearModelAction = PayloadAction<{
    key: string
    prefixes: ModelPrefix[]
    exclude: string
}>

export type MergeModelAction = PayloadAction<{
    combine: unknown
}>

export type CopyAction = PayloadAction<{
    source: ModelsPayload
    target: ModelsPayload
    mode: 'replace' | 'merge' | 'add'
    sourceMapper?: string
}>

export type FormInitAction = PayloadAction<ModelsPayload & {
    model: object
    formFirstInit: boolean
}>

export type AppendFieldToArrayAction = PayloadAction<FieldPath & {
    primaryKey?: string
    value?: object
}>

export type RemoveFieldFromArrayAction = PayloadAction<FieldPath & {
    index: number | [number, number]
}>

export type CopyFieldArrayAction = PayloadAction<FieldPath & {
    index: number
    primaryKey?: string
}>
