/* eslint-disable camelcase */
import { ValidationsKey } from '../../core/validation/IValidation'
import { DEPENDENCY_TYPES } from '../../core/dependencyTypes'

export type Field = {
    isInit: boolean
    visible: boolean
    visible_field: boolean
    visible_set: boolean
    disabled: boolean
    disabled_field: boolean
    disabled_set: boolean
    message: string | null
    filter: unknown // TODO: добавить тип
    dependency: Array<{
        type: `${DEPENDENCY_TYPES}`
        expression: string
        applyOnInit: boolean
        on: string[]
    }>
    required: boolean
    loading: boolean
    touched?: boolean
    parentIndex?: number
}

export type Form = {
    formName: string
    datasource: string
    modelPrefix: string
    validationKey: ValidationsKey
    dirty: boolean
    fields: Record<string, Field>
    prompt: boolean
}

export type FormsState = Record<string, Form>
