import reduce from 'lodash/reduce'
import { call, put } from 'redux-saga/effects'

import { disableWidget, enableWidget, hideWidget, showWidget } from '../../ducks/widgets/store'
import propsResolver from '../../utils/propsResolver'
import { DEPENDENCY_TYPES } from '../../core/dependencyTypes'

import { Model, OptionsType } from './WidgetTypes'

export const reduceFunction = (isTrue: boolean, { model, config }: Model) => isTrue && propsResolver(`\`${config?.condition}\``, model)

/**
 * Резолв видимости
 */

export function* resolveVisible(widgetId: string, model: OptionsType) {
    // @ts-ignore import from js file
    const visible = reduce(model, reduceFunction, true)

    if (visible) {
        yield put(showWidget(widgetId))
    } else {
        yield put(hideWidget(widgetId))
    }
}

/**
 * Резолв активности
 */
export function* resolveEnabled(widgetId: string, model: OptionsType) {
    // @ts-ignore import from js file
    const enabled = reduce(model, reduceFunction, true)

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
    model: OptionsType,
) {
    switch (dependencyType) {
        case DEPENDENCY_TYPES.visible: {
            yield call(resolveVisible, widgetId, model)

            break
        }
        case DEPENDENCY_TYPES.enabled: {
            yield call(resolveEnabled, widgetId, model)

            break
        }
        default:
            break
    }
}
