import { Field } from './types'

export const getDefaultState = () => ({
    dirty: false,
    fields: {},
    formName: null,
} as const)

export const getDefaultField = (): Field => ({
    isInit: false,
    visible: true,
    visible_field: true,
    visible_set: true,
    disabled: false,
    disabled_field: false,
    disabled_set: false,
    filter: [],
    dependency: [],
    required: false,
    loading: false,
    message: null,
    touched: false,
    isActive: false,
})
