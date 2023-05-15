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
 */
export const makeFieldByName = (prefix, formName, fieldName) => createSelector(
    makeFormByName(formName),
    form => get(form, [prefix, 'registeredFields', fieldName], {}),
)

/**
 * селектор для значения видимости поля
 */
export const isVisibleSelector = (prefix, formName, fieldName) => createSelector(
    makeFieldByName(prefix, formName, fieldName),
    field => field.visible,
)

/**
 * селектор для значения активности поля
 */
export const isDisabledSelector = (prefix, formName, fieldName) => createSelector(
    makeFieldByName(prefix, formName, fieldName),
    field => field.disabled,
)

/**
 * селектор для свойства, отвечающего за инициализацию дополнительных свойств
 */
export const isInitSelector = (prefix, formName, fieldName) => createSelector(
    makeFieldByName(prefix, formName, fieldName),
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

export const dependencySelector = (prefix, formName, fieldName) => createSelector(
    makeFieldByName(prefix, formName, fieldName),
    field => field.dependency,
)

export const filterSelector = (prefix, formName, fieldName) => createSelector(
    makeFieldByName(prefix, formName, fieldName),
    field => field.filter,
)

export const requiredSelector = (prefix, formName, fieldName) => createSelector(
    makeFieldByName(prefix, formName, fieldName),
    field => field.required,
)
/**
 * Селектор флага загрузки
 */
export const loadingSelector = (prefix, formName, fieldName) => createSelector(
    makeFieldByName(prefix, formName, fieldName),
    field => field.loading,
)

export const touchedSelector = (prefix, formName, fieldName) => createSelector(
    makeFieldByName(prefix, formName, fieldName),
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
