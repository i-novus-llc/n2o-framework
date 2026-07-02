import { select, call, takeEvery } from 'redux-saga/effects'

import { REGISTER_DEPENDENCY } from '../../constants/dependency'
import { getModelsByDependency } from '../models/selectors'
import { type State } from '../State'
import { resolveDependency } from '../../sagas/widgetDependency/resolve'
import { FullModelPath } from '../../core/models/types'
import { subscribe } from '../models/sagas/subscribe'
import { type WidgetsDependencies } from '../../sagas/widgetDependency/WidgetTypes'
import { type RegisterDependency } from '../../actions/dependency'

import { makeWidgetsDependenciesSelector } from './selectors'

/**
 * Обрабатывает зависимости ВСЕХ зарегистрированных виджетов.
 * Если передан колбэк isChanged, то резолвит только те зависимости,
 * у которых изменилась модель (используется в саге watcher).
 */
export function* resolveDependencies(isChanged: (path: FullModelPath) => boolean) {
    const state: State = yield select()
    const widgetsDependencies: WidgetsDependencies = yield select(makeWidgetsDependenciesSelector())

    for (const { dependency, widgetId } of Object.values(widgetsDependencies)) {
        for (const [depType, dep] of Object.entries(dependency || {})) {
            const options = getModelsByDependency(dep)(state)

            const shouldResolve = dep.some(d => isChanged(d.on))

            if (shouldResolve) {
                yield call(resolveDependency, depType, widgetId, options)
            }
        }
    }
}

/**
 * Обрабатывает зависимости для указанного виджета.
 */
export function* resolveWidgetDependencies(widgetId: string) {
    const state: State = yield select()

    const widgetsDependencies: WidgetsDependencies = yield select(makeWidgetsDependenciesSelector())

    const widgetDep = widgetsDependencies?.[widgetId]

    if (!widgetDep?.dependency) { return }

    for (const [depType, dep] of Object.entries(widgetDep.dependency)) {
        const options = getModelsByDependency(dep)(state)

        yield call(resolveDependency, depType, widgetId, options)
    }
}

/**
 * Подключение к саге watcher
 */
subscribe(resolveDependencies)

export const sagas = [takeEvery(REGISTER_DEPENDENCY, ({ payload }: RegisterDependency) => resolveWidgetDependencies(payload.widgetId))]
