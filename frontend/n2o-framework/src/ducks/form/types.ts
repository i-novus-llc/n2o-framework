/* eslint-disable camelcase */
import { DEPENDENCY_TYPES } from '../../core/dependencyTypes'

export type TFormField = {
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
}

export type TForm = {
    dirty?: boolean
} & Record<string, TFormField>

export type TFormState = Record<string, TForm>
