import { createSelector } from '@reduxjs/toolkit'
import { get } from 'lodash'

import { ModelPrefix } from '../../core/datasource/const'
import type { State as GlobalState } from '../State'

/**
 * Базовый селектор всех моделей
 * @param state
 * @return GlobalState
 */
const getGlobalState = (state: GlobalState) => state

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
const getModelSelector = (modelLink: string) => (state: GlobalState) => get(state, modelLink)

/**
 * Селектор получения данных из глобал стора по линкуy
 */
const getGlobalFieldByPath = (path: string) => createSelector(
    [
        getGlobalState,
    ],
    state => get(state, path, null),
)
/**
 * Селектор получения данных из модели по линку
 */
const getModelFieldByPath = (path: string) => createSelector(
    [
        modelsSelector,
    ],
    modelsState => get(modelsState, path, null),
)

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

export type Model = Record<string, unknown>
export type ModelTypeByPrefix<P extends ModelPrefix> = P extends ModelPrefix.source | ModelPrefix.selected ? Model[] : Model

/**
 * Селектор-генератор для получения конкретной модели
 */
function getModelByPrefixAndNameSelector<
    Prefix extends ModelPrefix,
    R extends ModelTypeByPrefix<Prefix> = ModelTypeByPrefix<Prefix>,
>(
    prefix: Prefix,
    fieldKey: string,
    defaultValue?: R,
) {
    return createSelector(
        [
            makeModelsByPrefixSelector(prefix),
        ],
        prefixModelsState => (prefixModelsState[fieldKey] || defaultValue) as R,
    )
}

export {
    modelsSelector,
    makeModelsByPrefixSelector,
    getModelByPrefixAndNameSelector,
    getGlobalFieldByPath,
    getModelsByDependency,
    getModelFieldByPath,
    getModelSelector,
}
