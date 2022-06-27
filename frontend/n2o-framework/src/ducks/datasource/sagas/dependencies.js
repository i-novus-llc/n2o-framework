import {
    put,
    select,
    fork,
} from 'redux-saga/effects'
import { get } from 'lodash'
import isEqual from 'lodash/isEqual'

import { DEPENDENCY_TYPE } from '../../../core/datasource/const'
import { dataRequest, startValidate } from '../store'
import { dataSourcesSelector } from '../selectors'
import { updateModel, setModel } from '../../models/store'

/**
 * @param {String} id
 * @param {DataSourceDependency} dependency
 * @param model
 */

export function* resolveDependency(id, dependency, model) {
    switch (dependency.type) {
        case DEPENDENCY_TYPE.fetch: {
            yield put(dataRequest(id))

            break
        }
        case DEPENDENCY_TYPE.validate: {
            yield put(startValidate(id))

            break
        }
        case DEPENDENCY_TYPE.copy: {
            const { model: targetPrefix, field: targetField } = dependency

            if (targetField) {
                yield put(updateModel(targetPrefix, id, targetField, model))
            } else {
                yield put(setModel(targetPrefix, id, model))
            }

            break
        }

        default: {
            // eslint-disable-next-line no-console
            console.warn(`unknown dependency type "${dependency.type}" for datasource "${id}"`)
        }
    }
}

/**
 * Сага наблюдения за зависимостями
 * @param action
 * @param {object} prevState
 */
export function* watchDependencies(action, prevState) {
    const state = yield select()
    const dataSources = yield select(dataSourcesSelector)
    const entries = Object.entries(dataSources)

    for (const [id, { dependencies }] of entries) {
        for (const dependency of dependencies) {
            const { on } = dependency
            const model = get(state, on)
            const prevModel = get(prevState, on)

            if (!isEqual(prevModel, model)) {
                yield fork(resolveDependency, id, dependency, model)
            }
        }
    }
}
