import reduce from 'lodash/reduce'
import { call, put } from 'redux-saga/effects'

import { disableWidget, enableWidget, hideWidget, showWidget } from '../../ducks/widgets/store'
import propsResolver from '../../utils/propsResolver'
import { DEPENDENCY_TYPES } from '../../core/dependencyTypes'

import { ResolveOption } from './WidgetTypes'

export const reduceFunction = (isTrue: boolean, { model, config }: ResolveOption) => isTrue && propsResolver<boolean>(`\`${config?.condition}\``, model)

/**
 * Резолв видимости
 */
export function* resolveVisible(widgetId: string, options: ResolveOption[]) {
    const visible = reduce(options, reduceFunction, true)

    if (visible) {
        yield put(showWidget(widgetId))
    } else {
        yield put(hideWidget(widgetId))
    }
}

/**
 * Резолв активности
 */
export function* resolveEnabled(widgetId: string, options: ResolveOption[]) {
    const enabled = reduce(options, reduceFunction, true)

    if (enabled) {
        yield put(enableWidget(widgetId))
    } else {
        yield put(disableWidget(widgetId))
    }
}

/**
 * Резолв конкретной зависимости по типу
 */
export function* resolveDependency(
    dependencyType: string,
    widgetId: string,
    options: ResolveOption[],
) {
    switch (dependencyType) {
        case DEPENDENCY_TYPES.visible: {
            yield call(resolveVisible, widgetId, options)

            break
        }
        case DEPENDENCY_TYPES.enabled: {
            yield call(resolveEnabled, widgetId, options)

            break
        }
        default:
            break
    }
}
