import { put, select, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import isObject from 'lodash/isObject'
import isEqual from 'lodash/isEqual'

import { EffectWrapper } from '../api/utils/effectWrapper'
import { executeExpression } from '../../core/Expression/execute'

import { appendToArray, copyModel, removeFromArray, setModel, updateModel } from './store'
import { getModelByPrefixAndNameSelector, Model } from './selectors'
import { CopyAction, FieldPath } from './Actions'

function getAction<T extends Model | Model[]>({
    model,
    newModel,
    validate,
    target,
}: {
    model: T
    newModel: T
    validate?: boolean
    target: FieldPath
}) {
    /*
     * Проверка, что в список был добавлен/удалён один элемент для изменения модели через removeFromArray/appendToArray
     * На которые дальше тригерятся механизмы сдвига метаданных валидаций и полей
     */
    if (Array.isArray(newModel) && newModel.length === 1 && !model) {
        return appendToArray({ ...target, value: newModel[0] })
    }
    if (Array.isArray(model) && model.length && !newModel?.length) {
        return removeFromArray({ ...target, start: 0, count: model.length })
    }
    if (Array.isArray(newModel) && Array.isArray(model)) {
        if ((newModel.length - model.length === 1)) {
            if (isEqual(newModel.slice(0, -1), model)) {
                // не нужно для сдвига метаданных, но нужно для срабатывания default-value
                return appendToArray({ ...target, value: newModel.at(-1) })
            }
            if (isEqual(newModel.slice(1), model)) {
                return appendToArray({ ...target, value: newModel[0], position: 0 })
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
                return removeFromArray({ ...target, start: left })
            }
        }
    }

    if (target.field) { return updateModel(target.prefix, target.key, target.field, newModel, validate) }

    return setModel(target.prefix, target.key, newModel, undefined, validate)
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
                // eslint-disable-next-line no-console
                console.warn('Source or target is not an array!')

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
    takeEvery(copyModel, EffectWrapper(copyAction)),
]
