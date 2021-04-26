import { createSelector } from 'reselect'
import isEmpty from 'lodash/isEmpty'

import { getWidgetId } from '../../tools/helpers'

/**
 * селектор всего виджета
 */
const widgetSelector = (state, props) => state.widgets[getWidgetId(props.pageId, props.containerId)] || {}

/**
 * селктор инициализации виджета
 */
const isInitSelector = createSelector(
    [widgetSelector],
    widgetState => widgetState.isInit,
)

/**
 * селектор видимости виджета
 */
const visibleSelector = createSelector(
    [widgetSelector],
    widgetState => widgetState.visible,
)

/**
 * секлектор энейбла виджета
 */
const enabledSelector = createSelector(
    [widgetSelector],
    widgetState => widgetState.enabled,
)

/**
 * селектор загрузки виджета
 */
const loadingSelector = createSelector(
    [widgetSelector],
    widgetState => widgetState.isLoading,
)

/**
 * селектор метаданных виджета
 */
const metadataSelector = createSelector(
    [widgetSelector],
    widgetState => widgetState.metadata,
)

/**
 * Селектор квэри
 */
const querySelector = createSelector(
    [widgetSelector],
    widgetState => widgetState.query,
)

/**
 * Селeктор резолв модели
 */
const resolveSelector = createSelector(
    [widgetSelector],
    widgetState => widgetState.resolveModel,
)

/**
 * Селeктор алертов
 */
const alertsSelector = createSelector(
    [widgetSelector],
    widgetState => widgetState.alerts,
)

const idsSelector = createSelector(
    [querySelector],
    queryState => (isEmpty(queryState) ? [] : queryState.list.map(m => m.id)),
)

const sortingSelector = createSelector(
    [widgetSelector],
    widgetState => widgetState.sorting,
)

export {
    widgetSelector,
    isInitSelector,
    visibleSelector,
    loadingSelector,
    metadataSelector,
    querySelector,
    resolveSelector,
    alertsSelector,
    idsSelector,
    sortingSelector,
}
