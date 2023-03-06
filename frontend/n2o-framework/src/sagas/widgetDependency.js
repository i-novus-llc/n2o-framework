import {
    select,
    call,
    takeEvery,
} from 'redux-saga/effects'
import keys from 'lodash/keys'
import isEqual from 'lodash/isEqual'
import cloneDeep from 'lodash/cloneDeep'
import sortBy from 'lodash/sortBy'
import get from 'lodash/get'

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
    modelInit,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
} from '../ducks/models/store'
import { DEPENDENCY_ORDER } from '../core/dependencyTypes'
import { getModelsByDependency } from '../ducks/models/selectors'
import { makeWidgetVisibleSelector } from '../ducks/widgets/selectors'

import { getWidgetDependency } from './widgetDependency/getWidgetDependency'
import { resolveDependency } from './widgetDependency/resolve'

let prevState = {}
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

export function* updateModelSaga({ type }) {
    const state = yield select()

    yield call(
        resolveWidgetDependency,
        type,
        prevState,
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
            const isVisible = yield select(makeWidgetVisibleSelector(widgetId))
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
                const dependentWidgetId = get(model, '[0].model.dependentWidgetId')

                yield call(
                    resolveDependency,
                    widgetDependenciesKeys[j],
                    widgetId,
                    model,
                    isVisible,
                    dependentWidgetId,
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
        modelInit,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], updateModelSaga),
    takeEvery(
        [
            setModel,
            removeModel,
            removeAllModel,
            copyModel,
            clearModel,
        ],
        function* noWidgetRecursion() {
            // Костыль, для сохранения предыдущего состояния, нужен чтобы не загнаться в рекурсивное обновление
            prevState = cloneDeep(yield select())
        },
    ),
]
