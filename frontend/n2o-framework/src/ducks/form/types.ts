import { ValidationResult, ValidationsKey } from '../../core/validation/types'
import { FormModelPrefix } from '../../core/models/types'

export enum FieldDependencyTypes {
    visible = 'visible',
    enabled = 'enabled',
    fetch = 'fetch',
    setValue = 'setValue',
    reset = 'reset',
    required = 'required',
    fetchValue = 'fetchValue',
}

export type FieldDependency = {
    applyOnInit?: boolean
    dataProvider?: unknown
    enabled?: string | boolean
    expression?: string
    message?: string
    on?: string[]
    type: FieldDependencyTypes
    valueFieldId?: string
    validate: boolean
}

export type Field = {
    isInit: boolean
    isActive: boolean
    visible: boolean
    visible_field: boolean
    visible_set: boolean
    disabled: boolean
    disabled_field: boolean
    disabled_set: boolean
    message: ValidationResult | null
    filter: unknown // TODO: добавить тип
    dependency: FieldDependency[]
    required: boolean
    loading: boolean
    // TODO @touched удалить
    touched?: boolean
    fetchTrigger?: number
    // FIXME костыль для прокидыания контекста из компонента для саг, придумать способ лучше
    ctx?: Record<string, unknown>
}

export type Form = {
    isInit: boolean
    formName: string
    datasource: string
    modelPrefix: FormModelPrefix
    validationKey: ValidationsKey
    dirty: boolean
    fields: Record<string, Field>
}

export type FormsState = Record<string, Form>
