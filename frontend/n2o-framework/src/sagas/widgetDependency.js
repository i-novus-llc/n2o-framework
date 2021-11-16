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
} from '../ducks/models/store'
import { DEPENDENCY_ORDER } from '../core/dependencyTypes'
import { getModelsByDependency } from '../ducks/models/selectors'
import { makeWidgetVisibleSelector } from '../ducks/widgets/selectors'

import { getWidgetDependency } from './widgetDependency/getWidgetDependency'
import { resolveDependency } from './widgetDependency/resolve'

let prevState = {}
let widgetsDependencies = {}

export function* registerDependency({ payload }) {
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
        prevState,
        state,
        widgetsDependencies,
    )
}

export function* updateModel() {
    const state = yield select()

    yield call(
        resolveWidgetDependency,
        prevState,
        state,
        widgetsDependencies,
    )
}

/**
 * Резолв всех зависимостей виджета
 * @param prevState
 * @param state
 * @param widgetsDependencies
 * @returns {IterableIterator<*|CallEffect>}
 */
export function* resolveWidgetDependency(
    prevState,
    state,
    widgetsDependencies,
) {
    const dependenciesKeys = sortBy(keys(widgetsDependencies), item => DEPENDENCY_ORDER.indexOf(item))

    for (let i = 0; i < dependenciesKeys.length; i++) {
        const { dependency, widgetId } = widgetsDependencies[dependenciesKeys[i]]
        const widgetDependenciesKeys = sortBy(keys(dependency), item => DEPENDENCY_ORDER.indexOf(item))

        for (let j = 0; j < widgetDependenciesKeys.length; j++) {
            const isVisible = yield select(makeWidgetVisibleSelector(widgetId))
            const prevModel = getModelsByDependency(
                dependency[widgetDependenciesKeys[j]],
            )(prevState)
            const model = getModelsByDependency(
                dependency[widgetDependenciesKeys[j]],
            )(state)

            if (!isEqual(prevModel, model)) {
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
        setModel.type,
        removeModel.type,
        removeAllModel.type,
        copyModel.type,
        clearModel.type,
    ], updateModel),
    takeEvery(
        [
            setModel.type,
            removeModel.type,
            removeAllModel.type,
            copyModel.type,
            clearModel.type,
            REGISTER_DEPENDENCY,
        ],
        function* noWidgetRecursion() {
            // Костыль, для сохранения предыдущего состояния, нужен чтобы не загнаться в рекурсивное обновление
            prevState = cloneDeep(yield select())
        },
    ),
]
