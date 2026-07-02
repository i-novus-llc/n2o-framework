import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'

import { State } from '../State'
import { dataSourceFieldError } from '../datasource/selectors'
import { FieldLink, ModelLink, ModelPrefix } from '../../core/models/types'
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
    formsState => get(formsState, name, null),
)

export const makeFormsByModelLink = (modelLink: ModelLink) => createSelector(
    formsSelector,
    forms => Object.values(forms).filter(({ modelLink: { id, prefix, index } }) => (
        // может прийти объект с index:undefined, от чего _.isEqual возвращает не то что ожидаем
        (id === modelLink.id) && (prefix === modelLink.prefix) && (index === modelLink.index)
    )),
)

// @deprecated
export const makeFormsByModel = (id: string, prefix: ModelPrefix) => (
    makeFormsByModelLink({ id, prefix })
)

export const getFormsFields = (modelLink: ModelLink) => createSelector(
    makeFormsByModelLink(modelLink),
    forms => forms.map(form => get(form, 'fields', {})).reduce((acc, fields) => ({ ...acc, ...fields }), {}),
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
    getFormFieldsByName(formName),
    fields => get(fields, fieldName, defaultField),
)

export const makeFieldParam = (formName: string, fieldName: string, key: keyof Field) => createSelector(
    makeFieldByName(formName, fieldName),
    field => (field[key]),
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
    form => Boolean(form?.dirty),
)

export const messageSelector = (fieldLink: FieldLink, formId = fieldLink.id) => createSelector(
    makeFieldByName(formId, fieldLink.field),
    dataSourceFieldError(fieldLink),
    (field: Field, errors: ValidationResult[] | undefined) => (field.message || errors?.[0]),
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
