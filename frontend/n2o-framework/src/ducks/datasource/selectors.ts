import { createSelector } from '@reduxjs/toolkit'

import { modelsSelector } from '../models/selectors'
import { ModelPrefix } from '../../core/datasource/const'
import type { State as GlobalState } from '../State'
import type { State as ModelsState } from '../models/Models'
import { ValidationsKey } from '../../core/validation/IValidation'
import { EMPTY_ARRAY, EMPTY_OBJECT } from '../../utils/emptyTypes'

export const dataSourcesSelector = (state: GlobalState) => state.datasource

export const dataSourceByIdSelector = (sourceId: string) => createSelector(
    dataSourcesSelector,
    sources => (sources[sourceId] || EMPTY_OBJECT),
)

/* eslint-disable indent */
export const dataSourceModelsSelector = <
    TModel extends object = object,
    TFilter extends object = object
>(sourceId: string) => createSelector(
    // @ts-ignore Не понятно почему ругается, заглушил для быстрого фикса
    modelsSelector,
    (modelsList: ModelsState<TModel, TFilter>) => ({
        [ModelPrefix.active]: modelsList[ModelPrefix.active][sourceId],
        [ModelPrefix.edit]: modelsList[ModelPrefix.edit][sourceId],
        [ModelPrefix.source]: modelsList[ModelPrefix.source][sourceId] || EMPTY_ARRAY,
        [ModelPrefix.selected]: modelsList[ModelPrefix.selected][sourceId] || EMPTY_ARRAY,
        [ModelPrefix.filter]: modelsList[ModelPrefix.filter][sourceId] || EMPTY_OBJECT,
    }),
)
/* eslint-enable indent */

export const dataSourceLoadingSelector = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.loading,
)

export const dataSourcePagingSelector = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.paging || {},
)

export const dataSourceSizeSelector = (sourceId: string) => createSelector(
    dataSourcePagingSelector(sourceId),
    state => state.size,
)

export const dataSourcePageSelector = (sourceId: string) => createSelector(
    dataSourcePagingSelector(sourceId),
    state => state.page,
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

export const dataSourceErrors = (
    sourceId: string,
    prefix: ModelPrefix.active | ModelPrefix.edit = ModelPrefix.active,
) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => (state.errors?.[prefix] || EMPTY_OBJECT),
)

export const dataSourceFieldError = (
    sourceId: string,
    prefix: ModelPrefix.active | ModelPrefix.edit,
    field: string,
) => createSelector(
    dataSourceErrors(sourceId, prefix),
    errors => errors[field],
)

export const dataSourceError = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => (state.error || EMPTY_OBJECT),
)

export const dataSourceAdditionalInfo = (sourceId: string) => createSelector(
    dataSourceByIdSelector(sourceId),
    state => state.additionalInfo,
)

export const dataSourceModelByPrefixSelector = (id: string, prefix: ModelPrefix) => createSelector(
    [
        modelsSelector,
    ],
    model => model[prefix][id],
)
