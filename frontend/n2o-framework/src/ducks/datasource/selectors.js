import { createSelector } from '@reduxjs/toolkit'

import { modelsSelector } from '../models/selectors'

export const dataSourcesSelector = (state = {}) => state.datasource || {}

export const dataSourceByIdSelector = sourceId => createSelector(
    dataSourcesSelector,
    sources => (sources[sourceId] || {}),
)

//
export const dataSourceModelsSelector = sourceId => createSelector(
    modelsSelector,
    (modelsList) => {
        const models = {}

        Object.entries(modelsList).forEach(([key, value]) => {
            models[key] = value[sourceId]
        })

        return models
    },
)

export const dataSourceLoadingSelector = sourceId => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.loading,
)

export const dataSourceSizeSelector = sourceId => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.size,
)

export const dataSourcePageSelector = sourceId => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.page,
)

export const dataSourceSortingSelector = widgetId => createSelector(
    dataSourceByIdSelector(widgetId),
    state => state.sorting,
)

export const dataSourceValidationSelector = sourceId => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.validation,
)

export const getDataSourceFieldValidation = (sourceId, fieldId) => createSelector(
    dataSourceValidationSelector(sourceId),
    validation => validation[fieldId],
)

export const dataSourceProviderSelector = sourceId => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.provider,
)
