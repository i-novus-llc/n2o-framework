import { put, select, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import set from 'lodash/set'
import values from 'lodash/values'
import includes from 'lodash/includes'
import merge from 'lodash/merge'
import entries from 'lodash/entries'
import isObject from 'lodash/isObject'

import { EffectWrapper } from '../api/utils/effectWrapper'
import evalExpression from '../../utils/evalExpression'

import type { State as ModelsState } from './Models'
import { copyModel, setModel, updateModel } from './store'
import { getModelByPrefixAndNameSelector, modelsSelector } from './selectors'
import { CopyAction } from './Actions'

export function* copyAction({ payload }: CopyAction) {
    const { target, source, mode = 'replace', sourceMapper: expression } = payload
    const state: ModelsState = yield select(modelsSelector)
    let sourceModel = get(state, values(source).join('.'))
    const selectedTargetModel: object[] = yield select(getModelByPrefixAndNameSelector(target.prefix, target.key))
    const targetModel = selectedTargetModel || []
    let newModel

    const { field } = target
    const targetModelField = get(targetModel, field, [])

    const treePath = includes(target.field, '.')

    if (expression) {
        sourceModel = evalExpression(expression, sourceModel)
    }

    if (mode === 'merge') {
        if (!target.field) {
            newModel = { ...targetModel, ...sourceModel }
        } else if (isObject(sourceModel) || Array.isArray(sourceModel)) {
            newModel = {
                ...targetModel,
                [target.field]: {
                    ...targetModelField,
                    ...sourceModel,
                },
            }
        } else {
            newModel = {
                ...targetModel,
                [target.field]: sourceModel,
            }
        }
    } else if (mode === 'add') {
        if (!Array.isArray(sourceModel) || !Array.isArray(targetModelField)) {
            // eslint-disable-next-line no-console
            console.warn('Source or target is not an array!')
        }

        if (!Array.isArray(sourceModel)) {
            /* the key by which the data is copied to the target model */
            if (field) {
                newModel = { ...targetModel, [field]: targetModelField.concat(sourceModel) }
            } else {
                newModel = targetModel.concat(sourceModel)
            }
        } else {
            sourceModel = Object.values(sourceModel)

            newModel = target.field
                ? {
                    ...targetModel,
                    [target.field]: [...targetModelField, ...sourceModel],
                }
                : [...targetModelField, ...sourceModel]
        }
    } else if (treePath) {
        newModel = merge({}, targetModel)
        set(newModel, target.field, sourceModel)
    } else {
        newModel = target.field
            ? {
                ...targetModel,
                [target.field]: sourceModel,
            }
            : sourceModel
    }

    yield put(setModel(target.prefix, target.key, newModel))

    // костыль, тк я убрал резолв зависимостей на redux-form/INITIALIZE из-за беспорядочных вызовов,
    // которые приводили к зацикливанию при applyOnInit: true и переинициализации, зависимости перестали вызываться
    // после копирования
    for (const [field, value] of entries(newModel)) {
        if (get(targetModel, field) !== value) {
            yield put(updateModel(target.prefix, target.key, field, value))
        }
    }
}

export const modelSagas = [
    takeEvery(copyModel, EffectWrapper(copyAction)),
]
