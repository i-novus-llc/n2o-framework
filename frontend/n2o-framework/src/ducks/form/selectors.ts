import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

import { State } from '../State'
import { dataSourceFieldError } from '../datasource/selectors'
import { ModelPrefix } from '../../core/datasource/const'
import { ValidationResult } from '../../core/validation/types'

import { getDefaultField } from './FormPlugin'
import { Field } from './types'

/**
 * селектор для редакс-форм
 */
export const formsSelector = (state: State) => state.form || {}

/**
 * селектор для  конкретной формы
 * @param name
 */
export const makeFormByName = (name: string) => createSelector(
    formsSelector,
    formsState => get(formsState, name) || {},
)

export const makeFormsByModel = (datasource: string, modelPrefix: ModelPrefix) => createSelector(
    formsSelector,
    forms => Object.values(forms).filter(form => (
        form.datasource === datasource &&
        form.modelPrefix === modelPrefix
    )),
)

export const getFormFieldsByName = (name: string) => createSelector(
    makeFormByName(name),
    form => get(form, 'fields', {}),
)

/**
 * селктор для поля формы
 */
const defaultField = Object.freeze(getDefaultField())

export const makeFieldByName = (formName: string, fieldName: string) => createSelector(
    makeFormByName(formName),
    form => get(form.fields, fieldName, defaultField),
)

export const makeFieldParam = (formName: string, fieldName: string, key: keyof Field) => createSelector(
    makeFormByName(formName),
    (form) => {
        const { fields } = form

        if (isEmpty(fields)) {
            return null
        }

        const field = fields[fieldName]

        if (isEmpty(field)) {
            return null
        }

        return field[key]
    },
)

/**
 * селектор для значения видимости поля
 */
export const isVisibleSelector = (formName: string, fieldName: string) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.visible,
)

/**
 * селектор для значения активности поля
 */
export const isDisabledSelector = (formName: string, fieldName: string) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.disabled,
)

/**
 * селектор для свойства, отвечающего за инициализацию дополнительных свойств
 */
export const isInitSelector = (formName: string, fieldName: string) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.isInit,
)

/**
 * селектор для свойства dirty
 */
export const isDirtyForm = (formName: string) => createSelector(
    makeFormByName(formName),
    form => Boolean(form.dirty),
)

export const messageSelector = (datasourceId: string, fieldName: string, modelPrefix: ModelPrefix) => createSelector(
    dataSourceFieldError(datasourceId, modelPrefix, fieldName),
    (errors: ValidationResult[] | undefined) => (errors?.[0]),
)

export const dependencySelector = (formName: string, fieldName: string) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.dependency,
)

export const filterSelector = (formName: string, fieldName: string) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.filter,
)

export const requiredSelector = (formName: string, fieldName: string) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.required,
)
/**
 * Селектор флага загрузки
 */
export const loadingSelector = (formName: string, fieldName: string) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.loading,
)

export const touchedSelector = (formName: string, fieldName: string) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.touched,
)
