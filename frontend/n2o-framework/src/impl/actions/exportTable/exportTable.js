import React from 'react'
import { getFormValues } from 'redux-form'

import { exportFormName } from '../../../components/widgets/Table/ExportModal'
import {
    makeWidgetPageSelector,
    makeWidgetSizeSelector,
    makeWidgetCountSelector,
} from '../../../selectors/widgets'
import { destroyOverlay } from '../../../actions/overlays'

/**
 * Функция для кодирования query
 * @param data
 * @returns {string}
 */
export function encodeQueryData(data) {
    const ret = []
    for (const d in data) { ret.push(`${encodeURIComponent(d)}=${encodeURIComponent(data[d])}`) }
    return `/export?${ret.join('&')}`
}

/**
 * Экшен экспорта таблицы
 * @param dispatch
 * @param state
 * @param widgetId
 */
export default function resolveExportTable({ dispatch, state, widgetId }) {
    const values = getFormValues(exportFormName)(state)
    const page =
    values.size === 'all' ? 1 : makeWidgetPageSelector(widgetId)(state)
    const size = makeWidgetSizeSelector(widgetId)(state)
    const count =
    values.size === 'all' ? makeWidgetCountSelector(widgetId)(state) : size
    window.open(
        encodeQueryData({
            widgetId,
            contentType: values.type,
            code: values.code,
            page,
            count,
        }),
        '_blank',
    )
    dispatch(destroyOverlay())
}
