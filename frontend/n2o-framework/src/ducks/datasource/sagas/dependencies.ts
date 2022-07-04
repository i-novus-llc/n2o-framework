import {
    put,
    select,
    fork,
} from 'redux-saga/effects'
import { get } from 'lodash'
import isEqual from 'lodash/isEqual'

import type { DataSourceDependency } from '../../../core/datasource/const'
import { DependencyTypes } from '../../../core/datasource/const'
import { dataRequest, startValidate } from '../store'
import { dataSourcesSelector } from '../selectors'
import { updateModel, setModel } from '../../models/store'
import type { State as GlobalState } from '../../State'
import type { State as DatasourceState } from '../DataSource'

/**
 * @param {String} id
 * @param {DataSourceDependency} dependency
 * @param model
 */
export function* resolveDependency(id: string, dependency: DataSourceDependency, model: unknown) {
    const { type } = dependency

    switch (type) {
        case DependencyTypes.fetch: {
            yield put(dataRequest(id))

            break
        }
        case DependencyTypes.validate: {
            yield put(startValidate(id))

            break
        }
        case DependencyTypes.copy: {
            const { model: targetPrefix, field: targetField } = dependency

            if (targetField) {
                yield put(updateModel(targetPrefix, id, targetField, model))
            } else {
                yield put(setModel(targetPrefix, id, model as object))
            }

            break
        }

        default: {
            // eslint-disable-next-line no-console
            console.warn(`unknown dependency type "${type}" for datasource "${id}"`)
        }
    }
}

/**
 * Сага наблюдения за зависимостями
 * @param action
 * @param {object} prevState
 */
export function* watchDependencies(action: unknown, prevState: GlobalState) {
    const state: GlobalState = yield select()
    const dataSources: DatasourceState = yield select(dataSourcesSelector)
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
