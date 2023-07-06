import { createSelector } from '@reduxjs/toolkit'

import {
    dataSourceCountSelector,
    dataSourcePageSelector,
    dataSourceSizeSelector,
    dataSourceSortingSelector,
} from '../datasource/selectors'
import { State } from '../State'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

export const widgetsSelector = (state: State) => state.widgets || EMPTY_OBJECT

/**
 * Селектор-генератор для получения виджета по ID
 * @param widgetId
 */
export const makeWidgetByIdSelector = (widgetId: string) => createSelector(
    widgetsSelector,
    widgets => widgets[widgetId] || EMPTY_OBJECT,
)

export const makeWidgetsByPageIdSelector = (pageId: string) => createSelector(
    widgetsSelector,
    widgets => Object.fromEntries(Object.entries(widgets).filter(([, widget]) => (widget.pageId === pageId))),
)

/**
 * Селектор-генератор для получения свойства виджета - isInit
 * @param widgetId
 */
export const makeWidgetIsInitSelector = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isInit,
)

/**
 * Селектор-генератор для получения свойства виджета - fetchOnInit
 * @param widgetId
 */
export const makeWidgetFetchOnInit = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.fetchOnInit,
)

/**
 * Селектор-генератор для получения свойства виджета - fetchOnVisibility
 * @param widgetId
 */
export const makeWidgetFetchOnVisibility = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.fetchOnVisibility,
)

export const makeDatasourceIdSelector = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.datasource,
)

/**
 * Селектор-генератор для получения свойства виджета - visible
 * @param widgetId
 */
export const makeWidgetVisibleSelector = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.visible,
)

/**
 * Селектор-генератор для получения свойства виджета - disabled
 * @param widgetId
 */
export const makeWidgetDisabledSelector = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.disabled,
)

export const makeModelIdSelector = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.modelId,
)

export const makeIsActiveSelector = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isActive,
)

export const makeWidgetPageIdSelector = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.pageId,
)

/**
 * Селектор-генератор для получения свойства виджета - isFilterVisible
 * @param widgetId
 */
export const makeWidgetFilterVisibilitySelector = (widgetId: string) => createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isFilterVisible,
)
export const makeFormModelPrefixSelector = (formName: string) => createSelector(
    makeWidgetByIdSelector(formName),
    widgetState => widgetState.form?.modelPrefix || 'resolve',
)

// eslint-disable-next-line @typescript-eslint/ban-types
const makeDatasourceSelector = (widgetId: string, makeSelector: Function) => (state: State) => {
    const sourceId = makeDatasourceIdSelector(widgetId)(state)

    return makeSelector(sourceId)(state)
}

/**
 * Селектор-генератор для получения свойства виджета - size
 * @param widgetId
 */
export const makeWidgetSizeSelector = (widgetId: string) => makeDatasourceSelector(widgetId, dataSourceSizeSelector)

/**
 * Селектор-генератор для получения свойства виджета - count
 * @param widgetId
 */
export const makeWidgetCountSelector = (widgetId: string) => makeDatasourceSelector(widgetId, dataSourceCountSelector)

/**
 * Селектор-генератор для получения свойства виджета - page
 * @param widgetId
 */
export const makeWidgetPageSelector = (widgetId: string) => makeDatasourceSelector(widgetId, dataSourcePageSelector)

/**
 * Селектор-генератор для получения свойства виджета - sorting
 * @param widgetId
 */
export const makeWidgetSortingSelector = (
    widgetId: string,
) => makeDatasourceSelector(widgetId, dataSourceSortingSelector)
