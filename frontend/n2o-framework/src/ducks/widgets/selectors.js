import { createSelector } from '@reduxjs/toolkit'

import {
    dataSourceCountSelector,
    dataSourcePageSelector,
    dataSourceSizeSelector,
    dataSourceSortingSelector,
} from '../datasource/selectors'

export const widgetsSelector = (state = {}) => state.widgets || {}

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

/**
 * Селектор-генератор для получения свойства виджета - fetchOnInit
 * @param widgetId
 */
export const makeWidgetFetchOnInit = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.fetchOnInit,
)

/**
 * Селектор-генератор для получения свойства виджета - fetchOnVisibility
 * @param widgetId
 */
export const makeWidgetFetchOnVisibility = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.fetchOnVisibility,
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

export const makeModelIdSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.modelId,
)

export const makeIsActiveSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isActive,
)

export const makeWidgetPageIdSelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.pageId,
)

/**
 * Селектор-генератор для получения свойства виджета - isFilterVisible
 * @param widgetId
 */
export const makeWidgetFilterVisibilitySelector = widgetId => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isFilterVisible,
)
export const makeFormModelPrefixSelector = formName => createSelector(
    makeWidgetByIdSelector(formName),
    widgetState => widgetState.form?.modelPrefix || 'resolve',
)

const makeDatasourceSelector = (widgetId, makeSelector) => (state) => {
    const sourceId = makeDatasourceIdSelector(widgetId)(state)

    return makeSelector(sourceId)(state)
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

/**
 * Селектор-генератор для получения свойства виджета - sorting
 * @param widgetId
 */
export const makeWidgetSortingSelector = widgetId => makeDatasourceSelector(widgetId, dataSourceSortingSelector)
