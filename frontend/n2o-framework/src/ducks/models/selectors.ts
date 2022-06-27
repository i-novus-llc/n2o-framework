import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'

import { ModelPrefix } from '../../core/datasource/const'
import type { State as GlobalState } from '../State'

/**
 * Базовый селектор всех моделей
 * @param state
 */
const modelsSelector = (state: GlobalState) => state.models || {}

/**
 * Селектор получения модели по линку
 * @param modelLink
 */
const getModelSelector = (modelLink: string) => (state: GlobalState) => get(state, modelLink)

const getModelsByDependency = (dependency: Array<{ on: string }>) => (state: GlobalState) => (
    dependency.map(config => ({
        model: getModelSelector(config.on)(state),
        config,
    }))
)

/**
 * Селектор-генератор для получения списка моделей по префиксу
 * @param prefix
 */
const makeModelsByPrefixSelector = (prefix: ModelPrefix) => createSelector(
    modelsSelector,
    modelsState => modelsState[prefix] || {},
)

/**
 * Селектор получения resolve моделей
 * @param state
 * @deprecated
 */
const resolveSelector = makeModelsByPrefixSelector(ModelPrefix.active)

/**
 * Селектор-генератор для получения конкретной модели
 * @param prefix
 * @param key
 */
const makeGetModelByPrefixSelector = (prefix: ModelPrefix, key: string) => createSelector(
    makeModelsByPrefixSelector(prefix),
    prefixModelsState => prefixModelsState[key],
)

/**
 * Селектор-генератор для получения resolve модели
 * @param key
 * @deprecated
 */
const makeGetResolveModelSelector = (key: string) => createSelector(
    resolveSelector,
    modelsState => modelsState[key],
)

export {
    modelsSelector,
    resolveSelector,
    makeModelsByPrefixSelector,
    makeGetModelByPrefixSelector,
    makeGetResolveModelSelector,
    getModelSelector,
    getModelsByDependency,
}
