import { ModelPrefix } from '../../core/datasource/const'
import { ValidationsKey } from '../../core/validation/types'

import { Field } from './types'

export const getDefaultState = () => ({
    datasource: null,
    dirty: false,
    fields: {},
    formName: null,
    modelPrefix: ModelPrefix.active,
    // Костыль для валидации фильтров
    validationKey: ValidationsKey.Validations,
})

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
