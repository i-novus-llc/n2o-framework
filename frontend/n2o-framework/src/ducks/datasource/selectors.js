import { createSelector } from '@reduxjs/toolkit'

import { modelsSelector } from '../models/selectors'
import { MODEL_PREFIX } from '../../core/datasource/const'

export const dataSourcesSelector = (state = {}) => state.datasource || {}

export const dataSourceByIdSelector = sourceId => createSelector(
    dataSourcesSelector,
    sources => (sources[sourceId] || {}),
)

//
export const dataSourceModelsSelector = sourceId => createSelector(
    modelsSelector,
    modelsList => ({
        [MODEL_PREFIX.active]: modelsList[MODEL_PREFIX.active][sourceId],
        [MODEL_PREFIX.source]: modelsList[MODEL_PREFIX.source][sourceId] || [],
        [MODEL_PREFIX.selected]: modelsList[MODEL_PREFIX.selected][sourceId] || [],
        [MODEL_PREFIX.filter]: modelsList[MODEL_PREFIX.filter][sourceId] || {},
    }),
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

export const dataSourceSortingSelector = sourceId => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.sorting,
)
export const dataSourceCountSelector = sourceId => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.count,
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

export const dataSourceErrors = sourceId => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.errors,
)

export const dataSourceFieldError = (sourceId, field) => createSelector(
    dataSourceErrors(sourceId),
    errors => errors[field],
)
