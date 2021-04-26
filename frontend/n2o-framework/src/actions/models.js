import {
    SET,
    REMOVE,
    REMOVE_ALL,
    SYNC,
    COPY,
    UPDATE,
    UPDATE_MAP,
    MERGE,
    CLEAR,
} from '../constants/models'

import createActionHelper from './createActionHelper'

/**
 * Установка значений модели по префиксу и ключу
 * @param prefix - префикс модели
 * @param key - уникальный ключ
 * @param model - значения модели
 * @example
 * dispatch(setModel("datasource", "Page.Widget", {id: 1, name: "Test"}))
 */
export function setModel(prefix, key, model) {
    return createActionHelper(SET)({ prefix, key, model })
}

/**
 * Обновление значения в модели
 * @param prefix - префикс модели
 * @param key - уникальный ключ
 * @param field - поле которое нужно изменить
 * @param value - новое значение
 * @returns {{type, prefix: *, key: *, values: *}}
 */
export function updateModel(prefix, key, field, value) {
    return createActionHelper(UPDATE)({ prefix, key, field, value })
}

/**
 * Удаление модели из хранилища
 * @param prefix - префикс модели
 * @param key - уникальный ключ
 * @example
 * dispatch(removeModel("datasource", "Page.Widget"))
 */
export function removeModel(prefix, key) {
    return createActionHelper(REMOVE)({ prefix, key })
}

/**
 * Удаление всех моделей из хранилища
 * @param key - уникальный ключ
 * @example
 * dispatch(removeModel("datasource", "Page.Widget"))
 */
export function removeAllModel(key) {
    return createActionHelper(REMOVE_ALL)({ key })
}

/**
 * Установка значений в несколько моделей
 * @experimental Этот экшен работает экспериментальном режиме
 * @param prefix - префикс модели
 * @param keys - массив уникальных ключей
 * @param model - модель
 */
export function syncModel(prefix, keys, model) {
    return createActionHelper(SYNC)({ prefix, keys, model })
}

/**
 * Копирование модели по префиксу и ключу в другую модель, по префиксу и ключу
 * @param {object} source - {prefix, key}
 * @param {object} target - {prefix, key}
 * @param {object} settings - {mode, sourceMapper}
 */
export function copyModel(source, target, { mode, sourceMapper }) {
    return createActionHelper(COPY)({
        sourceMapper,
        source,
        target,
        mode,
    })
}

/**
 * обновление массива с маппингом
 * @param prefix
 * @param key
 * @param field
 * @param value
 * @param map
 */
export function updateMapModel(prefix, key, field, value, map) {
    return createActionHelper(UPDATE_MAP)({ prefix, key, field, value, map })
}

export function combineModels(combine) {
    return createActionHelper(MERGE)({ combine })
}

/**
 * Очистка модели. которая учивает список исключений (поля которые не нужно очищать)
 * @param prefix
 * @param key
 * @param exclude
 */
export function clearModel(prefix, key, exclude = []) {
    return createActionHelper(CLEAR)({ prefix, key, exclude })
}
