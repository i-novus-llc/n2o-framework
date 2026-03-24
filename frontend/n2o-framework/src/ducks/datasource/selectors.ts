import { createSelector } from '@reduxjs/toolkit'

import { ModelPrefix } from '../../core/datasource/const'
import type { State as GlobalState } from '../State'
import { ValidationsKey } from '../../core/validation/types'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

export const dataSourcesSelector = (state: GlobalState) => state.datasource

export const dataSourceByIdSelector = (sourceId: string) => createSelector(
    dataSourcesSelector,
    sources => (sources[sourceId] || EMPTY_OBJECT),
)

export const dataSourceLoadingSelector = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.loading,
)

export const dataSourcePagingSelector = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.paging || {},
)

export const dataSourceDefaultPropsSelector = (
    sourceId: string,
) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.defaultDatasourceProps || {},
)

export const dataSourceSizeSelector = (sourceId: string) => createSelector(
    dataSourcePagingSelector(sourceId),
    state => state.size,
)

export const dataSourcePageSelector = (sourceId: string) => createSelector(
    dataSourcePagingSelector(sourceId),
    state => state.page,
)

export const dataSourcePageIdSelector = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.pageId,
)

export const dataSourceSortingSelector = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.sorting,
)

export const dataSourceCountSelector = (sourceId: string) => createSelector(
    dataSourcePagingSelector(sourceId),
    state => state.count,
)

export const dataSourceValidationSelector = (
    sourceId: string,
    validationsKey: ValidationsKey = ValidationsKey.Validations,
) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state[validationsKey],
)

export const getDataSourceFieldValidation = (sourceId: string, fieldId: string) => createSelector(
    dataSourceValidationSelector(sourceId),
    validations => validations[fieldId],
)

export const dataSourceProviderSelector = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.provider,
)

export const dataSourceComponentsSelector = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.components || [],
)

export const dataSourceProviderSizeSelector = (sourceId: string) => createSelector(
    dataSourceProviderSelector(sourceId),
    state => state?.size || null,
)

export const dataSourceErrors = (
    sourceId: string,
    prefix: ModelPrefix = ModelPrefix.active,
) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => (state.errors?.[prefix] || EMPTY_OBJECT),
)

export const dataSourceFieldError = (
    sourceId: string,
    prefix: ModelPrefix,
    field: string,
) => createSelector(
    dataSourceErrors(sourceId, prefix),
    errors => errors[field],
)

export const dataSourceError = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => (state.error || null),
)

export const dataSourceAdditionalInfo = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.additionalInfo,
)
