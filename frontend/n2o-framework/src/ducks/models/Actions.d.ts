import { Action } from '../Action'
import type { FormModelPrefix, Model, ModelLink, ModelPrefix } from '../../core/models/types'

import { State } from './Models'

export interface ModelsPayloadOld<T extends ModelPrefix = ModelPrefix> {
    prefix: T
    key: string
}

export type FieldPathOld = ModelsPayloadOld & {
    field?: string
}

export interface ModelsPayload<Prefix extends ModelPrefix = ModelPrefix> {
    modelLink: ModelLink<Prefix>
}

export type FieldPath = ModelsPayload & {
    fieldName: string
}

export type SetModelAction<Prefix extends ModelPrefix = ModelPrefix> = Action<string, ModelsPayload<Prefix> & {
    model: (Prefix extends (ModelPrefix.source | ModelPrefix.selected)
        ? Model[]
        : Model) | null
    isDefault?: boolean
}>

export type RemoveModelAction = Action<string, ModelsPayloadOld>

export type SyncModelAction = Action<string, {
    prefix: ModelPrefix
    keys: string[]
    model: object
}>

export type UpdateModelAction = Action<
    string,
    FieldPath & {
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
    source: FieldPathOld
    target: FieldPathOld
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
