import { call } from 'redux-saga/effects'

import { checkIdBeforeLazyFetch } from '../../regions/sagas'

export function* isActive(widgetId) {
    const { activeWidgetIds, tabsWidgetIds } = yield call(checkIdBeforeLazyFetch)

    return (
        tabsWidgetIds[widgetId] && activeWidgetIds.includes(widgetId)
    ) || (
        !Object.keys(tabsWidgetIds).includes(widgetId) ||
        !tabsWidgetIds[widgetId]
    )
}
