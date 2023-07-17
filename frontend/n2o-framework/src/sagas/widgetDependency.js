import {
    select,
    call,
    takeEvery,
} from 'redux-saga/effects'
import keys from 'lodash/keys'
import isEqual from 'lodash/isEqual'
import sortBy from 'lodash/sortBy'

import {
    REGISTER_DEPENDENCY,
} from '../constants/dependency'
import {
    clearModel,
    copyModel,
    removeModel,
    removeAllModel,
    setModel,
    updateModel,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
} from '../ducks/models/store'
import { DEPENDENCY_ORDER } from '../core/dependencyTypes'
import { getModelsByDependency } from '../ducks/models/selectors'

import { getWidgetDependency } from './widgetDependency/getWidgetDependency'
import { resolveDependency } from './widgetDependency/resolve'

let widgetsDependencies = {}

export function* registerDependency({ payload, type }) {
    const { widgetId, dependency } = payload
    const state = yield select()

    widgetsDependencies = yield call(
        getWidgetDependency,
        widgetsDependencies,
        widgetId,
        dependency,
    )
    yield call(
        resolveWidgetDependency,
        type,
        {},
        state,
        widgetsDependencies,
    )
}

export function* updateModelSaga({ type, meta }) {
    const state = yield select()

    yield call(
        resolveWidgetDependency,
        type,
        meta.prevState,
        state,
        widgetsDependencies,
    )
}

/**
 * Резолв всех зависимостей виджета
 * @param type
 * @param prevState
 * @param state
 * @param widgetsDependencies
 * @returns {IterableIterator<*|CallEffect>}
 * @template CallEffect
 */
export function* resolveWidgetDependency(
    type,
    prevState,
    state,
    widgetsDependencies,
) {
    const dependenciesKeys = sortBy(keys(widgetsDependencies), item => DEPENDENCY_ORDER.indexOf(item))

    for (let i = 0; i < dependenciesKeys.length; i++) {
        const { dependency, widgetId } = widgetsDependencies[dependenciesKeys[i]]
        const widgetDependenciesKeys = sortBy(keys(dependency), item => DEPENDENCY_ORDER.indexOf(item))

        for (let j = 0; j < widgetDependenciesKeys.length; j++) {
            const dep = dependency[widgetDependenciesKeys[j]]
            const prevModel = getModelsByDependency(dep)(prevState)
            const model = getModelsByDependency(dep)(state)
            const isFormActionType = [
                updateModel.type,
                appendFieldToArray.type,
                removeFieldFromArray.type,
                copyFieldArray.type,
            ].some(actionType => actionType === type)
            const isEqualModel = isFormActionType ? true : !isEqual(prevModel, model)

            if (isEqualModel) {
                yield call(
                    resolveDependency,
                    widgetDependenciesKeys[j],
                    widgetId,
                    model,
                )
            }
        }
    }
}

export const widgetDependencySagas = [
    takeEvery(REGISTER_DEPENDENCY, registerDependency),
    takeEvery([
        setModel,
        removeModel,
        removeAllModel,
        copyModel,
        clearModel,
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], updateModelSaga),
]
