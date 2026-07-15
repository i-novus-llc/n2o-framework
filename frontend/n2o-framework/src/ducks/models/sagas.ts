import { put, select, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import isObject from 'lodash/isObject'
import isEqual from 'lodash/isEqual'

import { AsyncEffectWrapper } from '../api/utils/effectWrapper'
import { executeExpression } from '../../core/Expression/execute'
import { logger } from '../../utils/logger'
import { ModelLink, ModelPrefix } from '../../core/models/types'

import { appendToArray, copyModel, removeFromArray, setModel, updateModel } from './store'
import { getModelByPrefixAndNameSelector, Model } from './selectors'
import { CopyAction, FieldPathOld } from './Actions'
import { sagas } from './sagas/watcher'

/**
 * Разбирает строку на имя поля и индекс (если строка начинается с паттерна [число].)
 * @param input - входная строка, например "[0].enabled" или "enabled"
 * @returns объект с полями fieldName (строка) и index (число или undefined)
 */
function parseIndexedField(input: string): { fieldName: string; index: number | undefined } {
    const match = input.match(/^\[(\d+)]\.(.+)/)

    if (match) {
        const index = parseInt(match[1], 10)
        const fieldName = match[2]

        return { fieldName, index }
    }

    // Если паттерн не найден, вся строка считается именем поля
    return { fieldName: input, index: undefined }
}

/**
 * Подготавливает модель связи и имя поля на основе строки с возможным индексом.
 * @param field - строка вида "[0].enabled" или просто "enabled"
 * @param prefix - префикс для ModelLink
 * @param id - идентификатор для ModelLink
 * @returns объект с полями modelLink (тип ModelLink) и fieldName (строка)
 */
function prepareModelLink(field: string, prefix: ModelPrefix, id: string): { modelLink: ModelLink; fieldName: string } {
    const modelLink: ModelLink = { prefix, id }
    const { fieldName, index } = parseIndexedField(field)

    if (index !== undefined) { modelLink.index = index }

    return { modelLink, fieldName }
}

function getAction<T extends Model | Model[]>({
    model,
    newModel,
    validate,
    target,
}: {
    model: T
    newModel: T
    validate?: boolean
    target: FieldPathOld
}) {
    /*
     * Проверка, что в список был добавлен/удалён один элемент для изменения модели через removeFromArray/appendToArray
     * На которые дальше тригерятся механизмы сдвига метаданных валидаций и полей
     */
    if (Array.isArray(newModel) && newModel.length === 1 && !model) {
        return appendToArray({
            modelLink: { prefix: target.prefix, id: target.key },
            fieldName: target.field,
            value: newModel[0],
        })
    }
    if (Array.isArray(model) && model.length && !newModel?.length) {
        return removeFromArray({
            modelLink: { prefix: target.prefix, id: target.key },
            fieldName: target.field,
            start: 0,
            count: model.length,
        })
    }
    if (Array.isArray(newModel) && Array.isArray(model)) {
        if ((newModel.length - model.length === 1)) {
            if (isEqual(newModel.slice(0, -1), model)) {
                // не нужно для сдвига метаданных, но нужно для срабатывания default-value
                return appendToArray({
                    modelLink: { prefix: target.prefix, id: target.key },
                    fieldName: target.field,
                    value: newModel.at(-1),
                })
            }
            if (isEqual(newModel.slice(1), model)) {
                return appendToArray({
                    modelLink: { prefix: target.prefix, id: target.key },
                    fieldName: target.field,
                    value: newModel[0],
                    position: 0,
                })
            }
        }
        if (newModel.length - model.length === -1) {
            let left = 0
            let right = 1

            while (left + right <= model.length) {
                const equalLeft = newModel[left] === model[left]
                const equalRight = newModel.at(-right) === model.at(-right)

                // move cursors while equals
                if (equalLeft) { left += 1 }
                if (equalRight) { right += 1 }

                if (!equalLeft && !equalRight) { break }
            }

            // if left & right cursors same => removed item, else => list has many changes
            if (left === model.length - right) {
                return removeFromArray({
                    modelLink: { prefix: target.prefix, id: target.key },
                    fieldName: target.field,
                    start: left,
                })
            }
        }
    }

    if (target.field) {
        const { modelLink, fieldName } = prepareModelLink(target.field, target.prefix, target.key)

        return updateModel(modelLink, fieldName, newModel, validate)
    }

    return setModel({ prefix: target.prefix, id: target.key }, newModel, undefined, validate)
}

// @ts-ignore FIXME validate должен быть в payload? а не на верхнем уровне
export function* copyAction({ payload, meta = {}, validate = true }: CopyAction) {
    const { target, source, mode = 'replace', sourceMapper: expression } = payload

    const targetModel: Model | Model[] = yield select(getModelByPrefixAndNameSelector(target.prefix, target.key))
    let targetField = target.field ? get(targetModel, target.field) : targetModel
    const sourceModel: Model | Model[] = yield select(getModelByPrefixAndNameSelector(source.prefix, source.key))
    let sourceField = source.field ? get(sourceModel, source.field) : sourceModel

    let newModel

    if (expression) {
        const { evalContext } = meta

        sourceField = executeExpression(expression, sourceField, evalContext)
    }

    switch (mode) {
        case 'merge': {
            newModel = isObject(sourceField)
                ? { ...targetField, ...sourceField }
                : sourceField

            break
        }
        case 'add': {
            if (!targetField) {
                targetField = []
            }
            if (!Array.isArray(targetField)) {
                logger.warn('Source or target is not an array!')

                return
            }

            newModel = Array.isArray(sourceField)
                ? [...targetField, ...sourceField]
                : [...targetField, sourceField]

            break
        }
        case 'replace':
        default: {
            newModel = sourceField
        }
    }

    yield put(getAction({ newModel, model: targetField, target, validate }))
}

export const modelSagas = [
    takeEvery([copyModel], AsyncEffectWrapper(copyAction)),
    ...sagas,
]
