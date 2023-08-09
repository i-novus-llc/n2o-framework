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
import { State } from '../ducks/State'

import { getWidgetDependency } from './widgetDependency/getWidgetDependency'
import { resolveDependency } from './widgetDependency/resolve'
import { Dependency, WidgetDependencies } from './widgetDependency/WidgetTypes'

let widgetsDependencies: WidgetDependencies = {}

interface RegisterDependencyPayload {
    widgetId: string
    dependency: Dependency
}
export function* registerDependency({ payload, type }: { payload: RegisterDependencyPayload, type: string }) {
    const { widgetId, dependency } = payload
    const state: State = yield select()

    // @ts-ignore FIXME не знаю как поправить
    widgetsDependencies = yield call(getWidgetDependency, widgetsDependencies, widgetId, dependency)
    // @ts-ignore FIXME не знаю как поправить
    yield call(resolveWidgetDependency, type, {}, state, widgetsDependencies)
}

export function* updateModelSaga({ type, meta }: { type: string, meta: { prevState: State } }) {
    const state: State = yield select()

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
    type: string,
    prevState: State,
    state: State,
    widgetsDependencies: WidgetDependencies,
) {
    const dependenciesKeys = sortBy(keys(widgetsDependencies), item => DEPENDENCY_ORDER.indexOf(item))

    for (let i = 0; i < dependenciesKeys.length; i += 1) {
        const key = dependenciesKeys[i]
        // @ts-ignore FIXME не знаю как поправить
        const { dependency, widgetId } = widgetsDependencies[key]
        const widgetDependenciesKeys = sortBy(keys(dependency), item => DEPENDENCY_ORDER.indexOf(item))

        for (let j = 0; j < widgetDependenciesKeys.length; j += 1) {
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
                // @ts-ignore FIXME не знаю как поправить
                yield call(resolveDependency, widgetDependenciesKeys[j], widgetId, model)
            }
        }
    }
}

export const widgetDependencySagas = [
    takeEvery(REGISTER_DEPENDENCY, registerDependency),
    // @ts-ignore Проблема с типизацией saga
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
