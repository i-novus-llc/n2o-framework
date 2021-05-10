import reduce from 'lodash/reduce'
import { call, put } from 'redux-saga/effects'

import { dataRequestWidget, disableWidget, enableWidget, hideWidget, showWidget } from '../../actions/widgets'
import propsResolver from '../../utils/propsResolver'
import { DEPENDENCY_TYPES } from '../../core/dependencyTypes'

export const reduceFunction = (isTrue, { model, config }) => isTrue && propsResolver(`\`${config.condition}\``, model)

/**
 * Резолв видимости
 * @param widgetId
 * @param model
 * @returns {IterableIterator<*>}
 */
export function* resolveVisible(widgetId, model) {
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
export function* resolveEnabled(widgetId, model) {
    const enabled = reduce(model, reduceFunction, true)

    if (enabled) {
        yield put(enableWidget(widgetId))
    } else {
        yield put(disableWidget(widgetId))
    }
}

/**
 * Резолв запросов
 * @param widgetId
 * @returns {IterableIterator<*>}
 */
export function* resolveFetch(widgetId) {
    yield put(dataRequestWidget(widgetId))
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
                    yield call(resolveFetch, widgetId)
                }
            } else if (isVisible) {
                yield call(resolveFetch, widgetId)
            }

            break
        }
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
