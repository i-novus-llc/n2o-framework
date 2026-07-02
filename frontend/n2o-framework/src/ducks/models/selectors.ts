import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'

import { ModelPrefix, Model, ModelTypeByPrefix, FullModelPath, ModelLink, FieldLink } from '../../core/models/types'
import type { State as GlobalState } from '../State'

/**
 * Базовый селектор всех моделей
 * @param state
 * @return GlobalState['models']
 */
const modelsSelector = (state: GlobalState) => state.models || {}

/**
 * Селектор получения модели по линку
 * @param modelLink
 */
const getModelSelector = <T extends Model | Model[]>(modelLink: ModelLink) => createSelector(
    modelsSelector,
    (modelsState) => {
        const model = modelsState[modelLink.prefix]?.[modelLink.id]

        if (modelLink.index !== undefined && Array.isArray(model)) {
            return model[modelLink.index] as T ?? null
        }

        return model as T ?? null
    },
)

function isFieldLink(link: ModelLink | FieldLink): link is FieldLink {
    return ('field' in link) && (typeof link.field === 'string')
}

export const getByLinkSelector = (link: ModelLink | FieldLink) => createSelector(
    getModelSelector(link),
    model => (
        isFieldLink(link)
            ? get(model, link.field, null)
            : model
    ),
)

export const getModelByFullPathSelector = (modelLink: FullModelPath) => (state: GlobalState) => get(state, modelLink, null)

/**
 * Селектор получения данных из модели по линку
 */
const getModelFieldByPath = (path: string) => createSelector(
    modelsSelector,
    modelsState => get(modelsState, path, null),
)

// @deprecated
const getModelsByDependency = <T extends { on: string }>(dependency: T[]) => (state: GlobalState) => (
    dependency.map(config => ({
        model: getModelByFullPathSelector(config.on as FullModelPath)(state),
        config,
    }))
)

/**
 * Селектор-генератор для получения списка моделей по префиксу
 * @param prefix
 * @deprecated
 */
const makeModelsByPrefixSelector = (prefix: ModelPrefix) => createSelector(
    modelsSelector,
    modelsState => modelsState[prefix] || {},
)

export type { Model, ModelTypeByPrefix }

/**
 * Селектор-генератор для получения конкретной модели
 */
function getModelByPrefixAndNameSelector<
    Prefix extends ModelPrefix,
    R extends ModelTypeByPrefix<Prefix> = ModelTypeByPrefix<Prefix>,
>(
    prefix: Prefix,
    id: string,
    defaultValue?: R,
) {
    return createSelector(
        getModelSelector<R>({ prefix, id }),
        model => model ?? defaultValue,
    )
}

export {
    modelsSelector,
    makeModelsByPrefixSelector,
    getModelByPrefixAndNameSelector,
    getModelsByDependency,
    getModelFieldByPath,
    getModelSelector,
}
