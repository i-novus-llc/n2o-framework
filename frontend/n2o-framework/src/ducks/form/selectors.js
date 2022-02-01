import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'

import { dataSourceFieldError } from '../datasource/selectors'
import { makeDatasourceIdSelector, makeFormModelPrefixSelector } from '../widgets/selectors'

/**
 * селектор для редакс-форм
 * @param state
 * @returns {{}}
 */
export const formsSelector = state => state.form || {}

/**
 * селектор для  конкретной формы
 * @param name
 */
export const makeFormByName = name => createSelector(
    formsSelector,
    formsState => get(formsState, name) || {},
)

/**
 * селктор для поля формы
 * @param formName
 * @param fieldName
 */
export const makeFieldByName = (formName, fieldName) => createSelector(
    makeFormByName(formName),
    form => get(form, ['registeredFields', fieldName], {}),
)

/**
 * селектор для значения видимости поля
 * @param formName
 * @param fieldName
 */
export const isVisibleSelector = (formName, fieldName) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.visible,
)

/**
 * селектор для значения активности поля
 * @param formName
 * @param fieldName
 */
export const isDisabledSelector = (formName, fieldName) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.disabled,
)

/**
 * селектор для свойства, отвечающего за инициализацию дополнительных свойств
 * @param formName
 * @param fieldName
 */
export const isInitSelector = (formName, fieldName) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.isInit,
)

export const messageSelector = (formName, fieldName) => createSelector(
    makeDatasourceIdSelector(formName),
    makeFormModelPrefixSelector(formName),
    state => state,
    (datasourceId, prefix, state) => {
        if (!datasourceId) {
            return undefined
        }

        return dataSourceFieldError(datasourceId, prefix, fieldName)(state)?.[0]
    },
)

export const dependencySelector = (formName, fieldName) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.dependency,
)

export const filterSelector = (formName, fieldName) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.filter,
)

export const requiredSelector = (formName, fieldName) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.required,
)
/**
 * Селектор флага загрузки
 * @param formName
 * @param fieldName
 * @return
 */
export const loadingSelector = (formName, fieldName) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.loading,
)

export const formValueSelector = (formName, fieldName) => createSelector(
    makeFormByName(formName),
    form => get(form, `values.${fieldName}`, []),
)
