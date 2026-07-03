import { createSelector } from '@reduxjs/toolkit'

import { ModelPrefix } from '../../core/models/types'
import type { State as GlobalState } from '../State'
import { FieldLink, ModelLink } from '../../core/models/types'

import { DataSource, Errors } from './DataSource'

export const dataSourcesSelector = (state: GlobalState) => state.datasource

export const dataSourceByIdSelector = (sourceId: string) => createSelector(
    dataSourcesSelector,
    sources => (sources[sourceId] || DataSource.defaultState),
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
    prefix: ModelPrefix = ModelPrefix.active,
) => createSelector(
    dataSourceByIdSelector(sourceId),
    (state) => {
        // Хак, чтобы не дублировать валидацию для форм с двумя моделями
        if (prefix === ModelPrefix.edit) {
            return state.validations[ModelPrefix.filter] ?? state.validations[ModelPrefix.active]
        }

        return state.validations[prefix]
    },
)

export const dataSourceProviderSelector = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.provider,
)

export const dataSourceProviderSizeSelector = (sourceId: string) => createSelector(
    dataSourceProviderSelector(sourceId),
    state => state?.size || null,
)

export const dataSourceErrors = <
    T extends ModelLink,
    R extends T extends { prefix: ModelPrefix.source | ModelPrefix.selected }
        ? T extends { index: number }
            ? Errors
            : Errors[]
        : Errors,
>({ id, prefix, index }: T) => createSelector(
        dataSourceByIdSelector(id),
        (state): R => {
        // Хак, чтобы не дублировать валидацию для форм с двумя моделями
            if (prefix === ModelPrefix.edit) {
                return (state.errors[ModelPrefix.filter] ?? state.errors[ModelPrefix.active]) as R
            }

            const isMulti = prefix === ModelPrefix.source || prefix === ModelPrefix.selected

            if (isMulti && typeof index === 'number') {
                return state.errors[prefix]?.[index] as R
            }

            return state.errors[prefix] as R
        },
    )

export const dataSourceFieldError = (fieldLink: FieldLink) => createSelector(
    dataSourceErrors(fieldLink),
    errors => errors?.[fieldLink.field],
)

export const dataSourceError = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => (state.error || null),
)

export const dataSourceAdditionalInfo = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.additionalInfo,
)

export const dataSourceSaveSettings = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.saveSettings,
)
