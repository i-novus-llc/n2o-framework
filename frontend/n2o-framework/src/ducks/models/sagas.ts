import { put, select, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import isObject from 'lodash/isObject'

import { EffectWrapper } from '../api/utils/effectWrapper'
import { executeExpression } from '../../core/Expression/execute'

import { copyModel, setModel, updateModel } from './store'
import { getModelByPrefixAndNameSelector, Model } from './selectors'
import { CopyAction } from './Actions'

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

    yield put(
        target.field
            ? updateModel(target.prefix, target.key, target.field, newModel, validate)
            : setModel(target.prefix, target.key, newModel, undefined, validate),
    )
}

export const modelSagas = [
    takeEvery(copyModel, EffectWrapper(copyAction)),
]
