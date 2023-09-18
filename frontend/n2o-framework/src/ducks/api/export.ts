import { createAction } from '@reduxjs/toolkit'
import get from 'lodash/get'
import { select, takeEvery, cancel } from 'redux-saga/effects'

// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { dataSourceByIdSelector } from '../datasource/selectors'
import { getContainerColumns } from '../columns/selectors'
import { Columns } from '../columns/Columns'
import { makeWidgetByIdSelector } from '../widgets/selectors'
import { Widget } from '../widgets/Widgets'
import { Action } from '../Action'
import { getModelSelector } from '../models/selectors'
import { ModelPrefix } from '../../core/datasource/const'
import { DataSourceState } from '../datasource/DataSource'
import { State } from '../State'

import { UTILS_PREFIX } from './constants'
import { EffectWrapper } from './utils/effectWrapper'
import { escapeUrl } from './utils/escapeUrl'

const ATTRIBUTES_ERROR = 'Ошибка экспорта, payload содержит не все параметры'
const PARAMS_ERROR = 'Ошибка экспорта, не передан формат или кодировка'
const IGNORE = 'ignore'
const PARAM_KEY = 'id'

export type Payload = {
    exportDatasource: string
    configDatasource: string
    baseURL: string
    widgetId: string
    allLimit: number
}

export const creator = createAction(
    `${UTILS_PREFIX}export`,
    (payload: Payload, meta: object) => ({
        payload,
        meta,
    }),
)

function getIgnored(columns: Columns, widget: Widget): string[] {
    const ignored = []

    const widgetColumns = get(widget, 'table.header.cells', [])

    for (const { id } of widgetColumns) {
        const visible = get(columns, `${id}.visible`)

        if (!columns[id] || !visible) {
            ignored.push(id)
        }
    }

    return ignored
}

function createExportUrl(
    resolvedURL: string,
    baseURL: string,
    format: string,
    charset: string,
    ignored: string[],
) {
    const { pathname } = window.location

    const escapedUrl = escapeUrl(resolvedURL)
    const path = pathname.slice(0, -1)

    let exportURL = `${path}${baseURL}?format=${format}&charset=${charset}&url=${escapedUrl}`

    if (!ignored.length) {
        return exportURL
    }

    for (const ignore of ignored) {
        exportURL += `&${IGNORE}=${ignore}`
    }

    return exportURL
}

interface ExportConfig {
    format: {
        [PARAM_KEY]: string
    }
    charset: {
        [PARAM_KEY]: string
    }
    type: {
        [PARAM_KEY]: 'all' | 'page'
        name: string
    }
}

export function* effect({ payload }: Action<string, Payload>) {
    const { exportDatasource, configDatasource, baseURL, widgetId, allLimit = 1000 } = payload

    if (!exportDatasource || !configDatasource || !baseURL || !widgetId) {
        // eslint-disable-next-line no-console
        console.error(ATTRIBUTES_ERROR)

        yield cancel()
    }

    const modelLink = `models.${ModelPrefix.active}.${configDatasource}`
    const model: ExportConfig = yield select(getModelSelector(modelLink))
    const { type, format: modelFormat, charset: modelCharset } = model

    const format = modelFormat[PARAM_KEY]
    const charset = modelCharset[PARAM_KEY]

    if (!format || !charset) {
        // eslint-disable-next-line no-console
        console.error(PARAMS_ERROR)

        yield cancel()
    }

    const state: State = yield select()

    const dataSource: DataSourceState = yield select(dataSourceByIdSelector(exportDatasource))
    const { provider, paging, sorting = {} } = dataSource
    const { url: resolvedURL } = dataProviderResolver(state, provider, {
        size: type[PARAM_KEY] === 'page' ? paging.size : allLimit,
        page: type[PARAM_KEY] === 'page' ? paging.page : 1,
        sorting,
    })

    const columns: Columns = yield select(getContainerColumns(widgetId))
    const widget: Widget = yield select(makeWidgetByIdSelector(widgetId))
    /* columns with failed security check or not visible */
    const ignored = getIgnored(columns, widget)

    const exportURL = createExportUrl(resolvedURL, baseURL, format, charset, ignored)

    window.open(exportURL, '_blank')
}

export const sagas = [takeEvery(creator.type, EffectWrapper(effect))]
