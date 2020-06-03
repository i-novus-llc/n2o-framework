/**
 * Created by emamoshin on 03.11.2017.
 */
import { createSelector } from 'reselect';
import get from 'lodash/get';
import { PREFIXES } from '../constants/models';

/*
  Базовые селекторы
*/

/**
 * Базовый селектор всех моделей
 * @param state
 */
const modelsSelector = state => {
  return state.models || {};
};

/**
 * Селектор получения resolve моделей
 * @param state
 */
const resolveSelector = state => {
  return state.models[PREFIXES.resolve] || {};
};

/**
 * Селектор получения filter моделей
 * @param state
 */
const filterSelector = state => {
  return state.models[PREFIXES.filter] || {};
};

/**
 * Селектор получения модели по линку
 * @param modelLink
 */
const getModelSelector = modelLink => state => {
  return get(state, modelLink);
};
/*
  Селекторы генераторы
*/

const getModelsByDependency = dependency => state => {
  return (
    dependency &&
    dependency.map(config => ({
      model: getModelSelector(config.on)(state),
      config,
    }))
  );
};

/**
 * Селектор-генератор для получения списка моделей по префиксу
 * @param prefix
 */
const makeModelsByPrefixSelector = prefix =>
  createSelector(
    modelsSelector,
    modelsState => {
      return modelsState[prefix] || {};
    }
  );

/**
 * Селектор-генератор для получения конкретной модели
 * @param prefix
 * @param key
 */
const makeGetModelByPrefixSelector = (prefix, key) =>
  createSelector(
    makeModelsByPrefixSelector(prefix),
    prefixModelsState => {
      return prefixModelsState[key];
    }
  );

/**
 * Селектор-генератор для получения resolve модели
 * @param key
 */
const makeGetResolveModelSelector = key =>
  createSelector(
    resolveSelector,
    modelsState => {
      return modelsState[key];
    }
  );

/**
 * Селектор-генератор для получения filter модели
 * @param key
 */
const makeGetFilterModelSelector = key =>
  createSelector(
    filterSelector,
    modelsState => {
      return modelsState[key];
    }
  );

/*
  Остальные селекторы
*/

export {
  modelsSelector,
  resolveSelector,
  makeModelsByPrefixSelector,
  makeGetModelByPrefixSelector,
  makeGetResolveModelSelector,
  makeGetFilterModelSelector,
  getModelSelector,
  getModelsByDependency,
};
