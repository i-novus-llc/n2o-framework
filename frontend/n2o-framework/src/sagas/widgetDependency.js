import {
    select,
    call,
    takeEvery,
} from 'redux-saga/effects'
import keys from 'lodash/keys'
import isEqual from 'lodash/isEqual'
import includes from 'lodash/includes'
import cloneDeep from 'lodash/cloneDeep'
import some from 'lodash/some'
import sortBy from 'lodash/sortBy'
import get from 'lodash/get'

import {
    REGISTER_DEPENDENCY,
    UPDATE_WIDGET_DEPENDENCY,
} from '../constants/dependency'
import { CLEAR, COPY, REMOVE, REMOVE_ALL, SET } from '../constants/models'
import { DEPENDENCY_ORDER } from '../core/dependencyTypes'
import { getModelsByDependency } from '../selectors/models'
import { makeWidgetVisibleSelector } from '../selectors/widgets'

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

export function* updateDependency({ payload }) {
    const { widgetId } = payload
    const state = yield select()
    const widgetDependenciesKeys = keys(widgetsDependencies)

    for (let i = 0; i < widgetDependenciesKeys.length; i++) {
        const widgetDependencyItem = widgetsDependencies[widgetDependenciesKeys[i]]
        const dependencyItem = widgetDependencyItem.dependency
        const dependencyItemKeys = keys(dependencyItem)

        for (let j = 0; j < dependencyItemKeys.length; j++) {
            const someDependency = dependencyItem[dependencyItemKeys[j]]

            if (some(someDependency, ({ on }) => includes(on, widgetId))) {
                const isVisible = makeWidgetVisibleSelector(widgetId)(state)
                const dependencyType = dependencyItemKeys[j]
                const model = getModelsByDependency(someDependency)(state)

                yield call(
                    resolveDependency,
                    dependencyType,
                    widgetDependencyItem.widgetId,
                    model,
                    isVisible,
                )
            }
        }
    }
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
    takeEvery(UPDATE_WIDGET_DEPENDENCY, updateDependency),
    takeEvery([SET, REMOVE, REMOVE_ALL, COPY, CLEAR], updateModel),
    takeEvery([SET, REMOVE, REMOVE_ALL, COPY, CLEAR, REGISTER_DEPENDENCY, UPDATE_WIDGET_DEPENDENCY], function* () {
        // Костыль, для сохранения предыдущего состояния, нужен чтобы не загнаться в рекурсивное обновление
        prevState = cloneDeep(yield select())
    }),
]
