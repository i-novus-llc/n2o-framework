import { Action, Meta } from '../Action'

import { Field, Form } from './types'

export interface FormPayload {
    formName: string
}

export type Tooltip = string | null

export type FormMeta = Meta & {
    validate?: boolean
}

export type FormAction<
    TPayload extends FormPayload,
    TMeta extends FormMeta = FormMeta,
> = Action<string, TPayload, TMeta>

export type RegisterAction = FormAction<{
    formName: string
    initState: Partial<Form>
}>

export type RemoveAction = FormAction<{
    formName: string
}>

export type FieldAction<T = unknown> = FormAction<T & {
    formName: string
    fieldName: string
}, FormMeta>

export type RegisterFieldAction = FieldAction<{
    initialState: Partial<Field>
}>

export type UnregisterFieldAction = FieldAction
export type FocusFieldAction = FieldAction
export type BlurFieldAction = FieldAction

export type SetFieldDisabledAction = FieldAction<{
    disabled: boolean
}>

export type SetFieldLoadingAction = FieldAction<{
    loading: boolean
}>

export type SetFieldRequiredAction = FieldAction<{
    required: boolean
}>

export type SetFieldVisibleAction = FieldAction<{
    visible: boolean
}>

export type SetFieldTooltipAction = FieldAction<{
    tooltip: Tooltip
}>

export type DangerouslySetFieldValue = FieldAction<{
    key: string
    value: unknown
}>

export type FieldsAction<T = unknown> = FormAction<T & {
    formName: string
    fields: string[]
}>

export type TouchFieldsAction = FieldsAction

export type SetMultiFieldDisabledAction = FieldsAction<{
    disabled: boolean
}>

export type SetMultiFieldVisibleAction = FieldsAction<{
    visible: boolean
}>

export type SetDirtyPayload = FormAction<{
    formName: string
    data: boolean
}>
