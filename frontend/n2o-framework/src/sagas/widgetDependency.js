import {
    select,
    fork,
    actionChannel,
    take,
    call,
    put,
} from 'redux-saga/effects'
import keys from 'lodash/keys'
import isEqual from 'lodash/isEqual'
import reduce from 'lodash/reduce'
import map from 'lodash/map'
import includes from 'lodash/includes'
import some from 'lodash/some'
import isEmpty from 'lodash/isEmpty'
import forOwn from 'lodash/forOwn'
import sortBy from 'lodash/sortBy'
import get from 'lodash/get'

import {
    REGISTER_DEPENDENCY,
    UPDATE_WIDGET_DEPENDENCY,
} from '../constants/dependency'
import { CLEAR, COPY, SET } from '../constants/models'
import { DEPENDENCY_TYPES, DEPENDENCY_ORDER } from '../core/dependencyTypes'
import {
    dataRequestWidget,
    showWidget,
    hideWidget,
    enableWidget,
    disableWidget,
} from '../actions/widgets'
import { getModelsByDependency } from '../selectors/models'
import { makeWidgetVisibleSelector } from '../selectors/widgets'
import propsResolver from '../utils/propsResolver'

export const reduceFunction = (isTrue, { model, config }) => isTrue && propsResolver(`\`${config.condition}\``, model)

export const sortDependency = (dependency) => {
    const tmpFetch = {}
    let newDependency = {}

    forOwn(dependency, (v, k) => {
        if (k !== 'fetch') {
            newDependency[k] = v
        } else {
            tmpFetch[k] = v
        }
    })

    if (!isEmpty(tmpFetch)) {
        newDependency = {
            ...newDependency,
            ...tmpFetch,
        }
    }

    return newDependency
}

/**
 * Наблюдение за регистрацией зависимостей виджетов и изменением модели
 * @returns {IterableIterator<*>}
 */
export function* watchDependency() {
    let widgetsDependencies = {}
    const channel = yield actionChannel([
        REGISTER_DEPENDENCY,
        SET,
        COPY,
        CLEAR,
        UPDATE_WIDGET_DEPENDENCY,
    ])
    while (true) {
        const prevState = yield select()
        const action = yield take(channel)
        const { type, payload } = action
        const { widgetId, dependency, key } = payload
        const state = yield select()
        switch (type) {
            case REGISTER_DEPENDENCY: {
                widgetsDependencies = yield call(
                    registerWidgetDependency,
                    widgetsDependencies,
                    widgetId,
                    dependency,
                )
                yield call(
                    resolveWidgetDependency,
                    prevState,
                    state,
                    widgetsDependencies,
                    widgetId,
                )
                break
            }
            case SET:
            case COPY:
            case CLEAR: {
                yield call(
                    resolveWidgetDependency,
                    prevState,
                    state,
                    widgetsDependencies,
                    key,
                )
                break
            }
            case UPDATE_WIDGET_DEPENDENCY: {
                yield call(forceUpdateDependency, state, widgetsDependencies, widgetId)
                break
            }
            default:
                break
        }
    }
}

export function* forceUpdateDependency(state, widgetsDependencies, widgetId) {
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
 * Добавляет в хранилище новопришедший виджет с его widgetId и dependency
 * @param widgetsDependencies
 * @param widgetId
 * @param dependency
 * @returns {{}}
 */
export function registerWidgetDependency(
    widgetsDependencies,
    widgetId,
    dependency,
) {
    if (dependency) {
        const parents = []

        dependency = sortDependency(dependency)

        map(dependency, (dep) => {
            map(dep, (d) => {
                if (d.on) {
                    parents.push(d.on)
                }
            })
        })

        return {
            ...widgetsDependencies,
            [widgetId]: {
                widgetId,
                dependency,
                parents,
            },
        }
    }

    return widgetsDependencies
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

/**
 * Резолв конкретной зависимости по типу
 * @param dependencyType
 * @param widgetId
 * @param model
 * @param isVisible
 * @param dependentWidgetId
 * @returns {IterableIterator<*|CallEffect>}
 */
export function* resolveDependency(
    dependencyType,
    widgetId,
    model,
    isVisible,
    dependentWidgetId,
) {
    switch (dependencyType) {
        case DEPENDENCY_TYPES.fetch: {
            if (dependentWidgetId) {
                if (widgetId === dependentWidgetId && isVisible) {
                    yield call(resolveFetchDependency, widgetId)
                }
            } else if (isVisible) {
                yield call(resolveFetchDependency, widgetId)
            }
            break
        }
        case DEPENDENCY_TYPES.visible: {
            yield call(resolveVisibleDependency, widgetId, model)
            break
        }
        case DEPENDENCY_TYPES.enabled: {
            yield call(resolveEnabledDependency, widgetId, model)
            break
        }
        default:
            break
    }
}

/**
 * Резолв запросов
 * @param widgetId
 * @returns {IterableIterator<*>}
 */
export function* resolveFetchDependency(widgetId) {
    yield put(dataRequestWidget(widgetId))
}

/**
 * Резолв видимости
 * @param widgetId
 * @param model
 * @returns {IterableIterator<*>}
 */
export function* resolveVisibleDependency(widgetId, model) {
    const visible = reduce(model, reduceFunction, true)
    if (visible) {
        yield put(showWidget(widgetId))
    } else {
        yield put(hideWidget(widgetId))
    }
}

/**
 * Резолв активности
 * @param widgetId
 * @param model
 * @returns {IterableIterator<*>}
 */
export function* resolveEnabledDependency(widgetId, model) {
    const enabled = reduce(model, reduceFunction, true)
    if (enabled) {
        yield put(enableWidget(widgetId))
    } else {
        yield put(disableWidget(widgetId))
    }
}

export const widgetDependencySagas = [fork(watchDependency)]
