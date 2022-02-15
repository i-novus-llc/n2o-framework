import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'

import { FORM, TABLE } from '../../components/widgets/widgetTypes'
import { dataSourceCountSelector, dataSourcePageSelector, dataSourceSizeSelector } from '../datasource/selectors'

/*
  Базовые селекторы
*/

/**
 * Базовый селектор всех виджетов
 * @param state
 */
export const widgetsSelector = (state = {}) => state.widgets || {}

/*
  Селекторы генераторы
*/

/**
 * Селектор-генератор для получения виджета по ID
 * @param widgetId
 */
export const makeWidgetByIdSelector = widgetId => createSelector(
    widgetsSelector,
    widgets => widgets[widgetId] || {},
)

/**
 * Селектор-генератор для получения свойства виджета - isInit
 * @param widgetId
 */
export const makeWidgetIsInitSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isInit,
)

export const makeDatasourceIdSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.datasource,
)

/**
 * Селектор-генератор для получения свойства виджета - visible
 * @param widgetId
 */
export const makeWidgetVisibleSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.visible,
)

/**
 * Селектор-генератор для получения свойства виджета - disabled
 * @param widgetId
 */
export const makeWidgetDisabledSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.disabled,
)

// region from datasource

const makeDatasourceSelector = (widgetId, makeSelector) => (state) => {
    const soueceId = makeDatasourceIdSelector(widgetId)(state)

    return makeSelector(soueceId)(state)
}

/**
 * Селектор-генератор для получения свойства виджета - size
 * @param widgetId
 */
export const makeWidgetSizeSelector = widgetId => makeDatasourceSelector(widgetId, dataSourceSizeSelector)

/**
 * Селектор-генератор для получения свойства виджета - count
 * @param widgetId
 */
export const makeWidgetCountSelector = widgetId => makeDatasourceSelector(widgetId, dataSourceCountSelector)

/**
 * Селектор-генератор для получения свойства виджета - page
 * @param widgetId
 */
export const makeWidgetPageSelector = widgetId => makeDatasourceSelector(widgetId, dataSourcePageSelector)

// endregion from datasource

export const makeWidgetPageIdSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.pageId,
)

/**
 * Селектор-генератор для получения свойства виджета - sorting
 * @param widgetId
 */
export const makeWidgetSortingSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.sorting,
)

/**
 * Селектор-генератор для получения свойства виджета - isFilterVisible
 * @param widgetId
 */
export const makeWidgetFilterVisibilitySelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isFilterVisible,
)

/**
 * Селектор-генератор для получения свойства виджета - sorting
 * @param widgetId
 */
export const makeWidgetValidationSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.validation,
)

export const getWidgetFieldValidation = (state, widgetId, fieldId) => get(state, ['widgets', widgetId, 'validation', fieldId])

export const makeModelIdSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.modelId,
)

export const makeIsActiveSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isActive,
)

export const makeTypeSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.type,
)

export const makeWidgetDataProviderSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.dataProvider,
)

export const isAnyTableFocusedSelector = createSelector(
    widgetsSelector,
    widgetsState => Object.values(widgetsState).some(
        widget => (widget.type === TABLE || widget.type === FORM) && widget.isActive,
    ),
)

// region others
export const makeFormModelPrefixSelector = formName => createSelector(
    makeWidgetByIdSelector(formName),
    widgetState => widgetState.form?.modelPrefix || 'resolve',
)
// endregion others
