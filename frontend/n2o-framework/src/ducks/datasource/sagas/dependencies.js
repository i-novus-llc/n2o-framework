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

/**
 * @param {String} id
 * @param {DataSourceDependency} dependency
 * @param model
 */
// eslint-disable-next-line no-unused-vars
export function* resolveDependency(id, dependency, model) {
    switch (dependency.type) {
        case DEPENDENCY_TYPE.fetch: {
            yield put(dataRequest(id))

            return
        }
        case DEPENDENCY_TYPE.validate: {
            yield put(startValidate(id))

            return
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

    // eslint-disable-next-line no-restricted-syntax
    for (const [id, { dependencies }] of entries) {
        // eslint-disable-next-line no-restricted-syntax
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
