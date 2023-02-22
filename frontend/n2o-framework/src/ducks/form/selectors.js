import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'
import filter from 'lodash/filter'

import { dataSourceFieldError } from '../datasource/selectors'
import { makeFormModelPrefixSelector, widgetsSelector } from '../widgets/selectors'

/**
 * селектор для редакс-форм
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

export const getFormFieldsByName = name => createSelector(
    makeFormByName(name),
    form => get(form, 'fields', {}),
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

/**
 * селектор для свойства dirty
 */
export const isDirtyForm = formName => createSelector(
    makeFormByName(formName),
    form => Boolean(form.dirty),
)

export const messageSelector = (datasourceId, fieldName, modelPrefix) => createSelector(
    makeFormModelPrefixSelector(datasourceId),
    state => state,
    (prefix, state) => {
        if (!datasourceId) {
            return undefined
        }

        return dataSourceFieldError(datasourceId, modelPrefix || prefix, fieldName)(state)?.[0]
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

export const touchedSelector = (formName, fieldName) => createSelector(
    makeFieldByName(formName, fieldName),
    field => field.touched,
)

export const formValueSelector = (formName, fieldName) => createSelector(
    makeFormByName(formName),
    form => get(form, `values.${fieldName}`, []),
)

export const makeFormsByDatasourceSelector = datasource => createSelector(
    widgetsSelector,
    widgets => filter(widgets, widgetState => widgetState.datasource === datasource && widgetState.form),
)

export const makeFormsFiltersByDatasourceSelector = datasource => createSelector(
    widgetsSelector,
    widgets => filter(widgets, widgetState => widgetState.datasource === datasource && widgetState.filter),
)
