/**
 * Created by emamoshin on 03.11.2017.
 */
import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'

import { PREFIXES } from './constants'

/**
 * Базовый селектор всех моделей
 * @param state
 */
const modelsSelector = state => state.models || {}

/**
 * Селектор получения resolve моделей
 * @param state
 */
const resolveSelector = state => state.models[PREFIXES.resolve] || {}

/**
 * Селектор получения модели по линку
 * @param modelLink
 */
const getModelSelector = modelLink => state => get(state, modelLink)
/*
  Селекторы генераторы
*/

const getModelsByDependency = dependency => state => (
    dependency &&
    dependency.map(config => ({
        model: getModelSelector(config.on)(state),
        config,
    }))
)

/**
 * Селектор-генератор для получения списка моделей по префиксу
 * @param prefix
 */
const makeModelsByPrefixSelector = prefix => createSelector(
    modelsSelector,
    modelsState => modelsState[prefix] || {},
)

/**
 * Селектор-генератор для получения конкретной модели
 * @param prefix
 * @param key
 */
const makeGetModelByPrefixSelector = (prefix, key) => createSelector(
    makeModelsByPrefixSelector(prefix),
    prefixModelsState => prefixModelsState[key],
)

/**
 * Селектор-генератор для получения resolve модели
 * @param key
 */
const makeGetResolveModelSelector = key => createSelector(
    resolveSelector,
    modelsState => modelsState[key],
)

/*
  Остальные селекторы
*/

export {
    modelsSelector,
    resolveSelector,
    makeModelsByPrefixSelector,
    makeGetModelByPrefixSelector,
    makeGetResolveModelSelector,
    getModelSelector,
    getModelsByDependency,
}
