// FIXME: зависимости виджета нуждаются в рефакторинге, переусложнено
import { select, call, takeEvery } from 'redux-saga/effects'
import isEqual from 'lodash/isEqual'

import { REGISTER_DEPENDENCY } from '../constants/dependency'
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
import { getModelsByDependency } from '../ducks/models/selectors'
import { State } from '../ducks/State'

import { getWidgetDependency } from './widgetDependency/getWidgetDependency'
import { resolveDependency } from './widgetDependency/resolve'
import {
    type Dependencies,
    type WidgetsDependencies,
} from './widgetDependency/WidgetTypes'

let widgetsDependencies: WidgetsDependencies = {}

interface RegisterDependencyPayload {
    widgetId: string
    dependency: Dependencies
}
export function* registerDependency({ payload, type }: { payload: RegisterDependencyPayload, type: string }) {
    const { widgetId, dependency } = payload
    const state: State = yield select()

    widgetsDependencies = yield call<typeof getWidgetDependency>(
        getWidgetDependency,
        widgetsDependencies,
        widgetId,
        dependency,
    )

    yield call<typeof resolveWidgetDependency>(
        resolveWidgetDependency,
        type,
        {} as State,
        state,
        widgetsDependencies,
    )
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
    widgetsDependencies: WidgetsDependencies,
) {
    for (const { dependency, widgetId } of Object.values(widgetsDependencies)) {
        for (const [depType, dep] of Object.entries(dependency || {})) {
            const prevOptions = getModelsByDependency(dep)(prevState)
            const options = getModelsByDependency(dep)(state)
            const isFormActionType = [
                updateModel.type,
                appendFieldToArray.type,
                removeFieldFromArray.type,
                copyFieldArray.type,
            ].some(actionType => actionType === type)
            const isChanged = isFormActionType ? true : !isEqual(prevOptions, options)

            if (isChanged || REGISTER_DEPENDENCY === type) {
                yield call<typeof resolveDependency>(
                    resolveDependency,
                    depType,
                    widgetId,
                    options,
                )
            }
        }
    }
}

export const widgetDependencySagas = [
    takeEvery(REGISTER_DEPENDENCY, registerDependency),
    takeEvery([
        setModel.type,
        removeModel.type,
        removeAllModel.type,
        copyModel.type,
        clearModel.type,
        updateModel.type,
        appendFieldToArray.type,
        removeFieldFromArray.type,
        copyFieldArray.type,
    ], updateModelSaga),
]
