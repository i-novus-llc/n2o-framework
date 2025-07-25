import { put, select, fork } from 'redux-saga/effects'
import get from 'lodash/get'
import isEqual from 'lodash/isEqual'

import { DataSourceDependency, DependencyTypes } from '../../../core/datasource/const'
import { dataRequest, startValidate, submit as submitAction } from '../store'
import { dataSourcesSelector, dataSourceByIdSelector } from '../selectors'
import { updateModel, setModel } from '../../models/store'
import { State as DatasourceState } from '../DataSource'
import { State as GlobalState } from '../../State'
import { RegisterAction } from '../Actions'
import { DefaultModels } from '../../models/Models'

/**
 * @param {String} id
 * @param {DataSourceDependency} dependency
 * @param model
 */
export function* resolveDependency(id: string, dependency: DataSourceDependency, model: DefaultModels) {
    const { type } = dependency

    switch (type) {
        case DependencyTypes.fetch: {
            yield put(dataRequest(id, { page: 1 }))

            break
        }
        case DependencyTypes.validate: {
            yield put(startValidate(id))

            break
        }
        case DependencyTypes.copy: {
            const { model: targetPrefix, field: targetField, submit: submitAfterCopy } = dependency

            if (targetField) {
                yield put(updateModel(targetPrefix, id, targetField, model))
            } else {
                yield put(setModel(targetPrefix, id, model as Record<string, unknown>))
            }

            if (submitAfterCopy) {
                const { submit: submitProvider } = yield dataSourceByIdSelector(id)

                if (submitProvider) { yield put(submitAction(id, submitProvider)) }
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
 */
let prevState: GlobalState | void

export function* watchDependencies() {
    const state: GlobalState = yield select()
    const dataSources: DatasourceState = yield select(dataSourcesSelector)
    const entries = Object.entries(dataSources)

    for (const [id, { dependencies }] of entries) {
        for (const dependency of dependencies) {
            const { on } = dependency
            const model = get(state, on)
            const prevModel = get(prevState, on)

            if (!isEqual(model, prevModel)) {
                yield fork(resolveDependency, id, dependency, model)
            }
        }
    }

    prevState = state
}

export function* applyOnInitDependencies({ payload }: RegisterAction) {
    const { id, initProps } = payload
    const { dependencies = [] } = initProps
    const state: GlobalState = yield select()

    for (const dependency of dependencies) {
        if (dependency.applyOnInit) {
            const model = get(state, dependency.on)

            yield fork(resolveDependency, id, dependency, model)
        }
    }
}
