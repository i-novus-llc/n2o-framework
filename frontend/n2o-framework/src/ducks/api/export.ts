import { createAction } from '@reduxjs/toolkit'
import { cancel, select, takeEvery } from 'redux-saga/effects'

import { dataProviderResolver } from '../../core/dataProviderResolver'
import { dataSourceByIdSelector } from '../datasource/selectors'
import { getTableColumns } from '../table/selectors'
import { Action } from '../Action'
import { getModelSelector } from '../models/selectors'
import { ModelPrefix } from '../../core/datasource/const'
import { DataSourceState } from '../datasource/DataSource'
import { State } from '../State'
import { Provider, ProviderType } from '../datasource/Provider'
import { logger } from '../../core/utils/logger'
import { Column, Columns } from '../columns/Columns'

import { UTILS_PREFIX } from './constants'
import { EffectWrapper } from './utils/effectWrapper'

const ATTRIBUTES_ERROR = 'Ошибка экспорта, payload содержит не все параметры'
const PARAMS_ERROR = 'Ошибка экспорта, не передан формат или кодировка'
const PARAM_KEY = 'id'
const INHERITED_SOURCE_FIELD_ID = 'source_field_id'
const PARENT_ROW_ID = 'parent_id'
const SORTING = 'sorting'

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

function getShowedColumnsWithLabels(columns: Column[]): Record<string, string> {
    const fields: Record<string, string> = {}

    columns
        .filter(column => column.visible && column.visibleState)
        .forEach((column) => {
            const id = column.columnId

            // @INFO Используем label если он указан, иначе id колонки
            fields[id] = column.label || column.columnId
        })

    return fields
}

function createExportPayload(
    resolvedURL: string,
    format: string,
    charset: string,
    fields: Record<string, string>,
) {
    return {
        format,
        charset,
        url: resolvedURL,
        fields,
    }
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

interface InheritedExtraParams {
    [INHERITED_SOURCE_FIELD_ID]?: string
    [PARENT_ROW_ID]?: string | number
    [SORTING]?: Record<string, unknown>
}

export function* effect({ payload }: Action<string, Payload>) {
    const { exportDatasource, configDatasource, baseURL, widgetId, allLimit = 1000 } = payload

    if (!exportDatasource || !configDatasource || !baseURL || !widgetId) {
        logger.error(ATTRIBUTES_ERROR)
        yield cancel()
    }

    const modelLink = `models.${ModelPrefix.active}.${configDatasource}`
    const model: ExportConfig = yield select(getModelSelector(modelLink))
    const { type, format: modelFormat, charset: modelCharset } = model

    const format = modelFormat[PARAM_KEY]
    const charset = modelCharset[PARAM_KEY]

    if (!format || !charset) {
        logger.error(PARAMS_ERROR)
        yield cancel()
    }

    const state: State = yield select()

    const dataSource: DataSourceState = yield select(dataSourceByIdSelector(exportDatasource))
    const { paging, provider, sorting = {} } = dataSource

    /* Исключение для inherited ds в таблице,
       необходимо подкидывать extra query params
       и заменять provider на родительский */
    let inheritedProvider: Provider | null = null
    const extraParams: InheritedExtraParams = {}

    if (provider && provider.type === ProviderType.inherited) {
        const { sourceDs } = provider
        const parentDs: DataSourceState = yield select(dataSourceByIdSelector(sourceDs))

        const { provider: parentProvider } = parentDs

        if (parentProvider) {
            const { sourceField, sourceModel } = provider

            inheritedProvider = { ...parentProvider }

            const { sorting } = parentProvider

            extraParams[INHERITED_SOURCE_FIELD_ID] = sourceField

            const { id }: { id: string | number } = yield select(getModelSelector(`models.${sourceModel}.${sourceDs}`))

            extraParams[PARENT_ROW_ID] = id
            extraParams[SORTING] = sorting
        }
    }

    const { url: resolvedURL } = dataProviderResolver(
        state,
        // @ts-ignore FIXME ругается на тип аргумента
        inheritedProvider || provider,
        {
            size: type[PARAM_KEY] === 'page' ? paging.size : allLimit,
            page: type[PARAM_KEY] === 'page' ? paging.page : 1,
            sorting,
            ...extraParams,
        },
    )

    /** @INFO Здесь разница с более поздними версиями в том, как хранятся столбцы и заголовки. */
    const columns: Columns = yield select(getTableColumns(widgetId))

    const fields = getShowedColumnsWithLabels(Object.values(columns))
    const exportPayload = createExportPayload(resolvedURL, format, charset, fields)

    try {
        const response: Response = yield fetch(baseURL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(exportPayload),
        })

        if (response.ok) {
            const blob: Blob = yield response.blob()
            const url = window.URL.createObjectURL(blob)
            const a = document.createElement('a')

            a.style.display = 'none'
            a.href = url
            a.download = `export.${format}`
            document.body.appendChild(a)
            a.click()
            window.URL.revokeObjectURL(url)
            document.body.removeChild(a)
        } else {
            logger.error(response.statusText)
        }
    } catch (error) {
        logger.error(error)
    }
}

export const sagas = [takeEvery(creator.type, EffectWrapper(effect))]
