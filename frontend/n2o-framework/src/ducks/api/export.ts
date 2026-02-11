import { createAction } from '@reduxjs/toolkit'
import { cancel, select, takeEvery } from 'redux-saga/effects'

import { dataProviderResolver } from '../../core/dataProviderResolver'
import { dataSourceByIdSelector } from '../datasource/selectors'
import { getTableCells } from '../table/selectors'
import { type Action } from '../Action'
import { getModelSelector } from '../models/selectors'
import { type DataSourceState } from '../datasource/DataSource'
import { type State } from '../State'
import { type Provider, ProviderType } from '../datasource/Provider'
import { BodyCell, type HeaderCell } from '../table/Table'
import { getAllValuesByKey } from '../../components/Table/utils'
import { logger } from '../../core/utils/logger'
import { linkResolver, type LinkProps } from '../../utils/linkResolver'

import { UTILS_PREFIX } from './constants'
import { EffectWrapper } from './utils/effectWrapper'

const ATTRIBUTES_ERROR = 'Ошибка экспорта, payload содержит не все параметры'
const PARAMS_ERROR = 'Ошибка экспорта, не передан формат или кодировка'
const INHERITED_SOURCE_FIELD_ID = 'source_field_id'
const PARENT_ROW_ID = 'parent_id'
const SORTING = 'sorting'

/** Колонки содержащие эти параметры не экспортируются
 * TODO при увеличении массива сделать 1 общий параметр прим. noExport */
const NON_EXPORTABLE_KEYS = ['moveMode']

enum Setting {
    FORMAT = 'format',
    CHARSET = 'charset',
    TYPE = 'type',
}

type Values = Record<Setting, LinkProps>

export interface Payload {
    exportDatasource: string
    values: Values
    baseURL: string
    widgetId: string
    allLimit: number
    filename?: string
}

export const creator = createAction(
    `${UTILS_PREFIX}export`,
    (payload: Payload, meta: object) => ({
        payload,
        meta,
    }),
)

function getShowedColumnsWithLabels(header: HeaderCell[], body: BodyCell[]) {
    return header
        .filter(column => column.visible && column.visibleState)
        .map(({ id, label }) => ({
            id,
            // @INFO Используем label если он указан, иначе id колонки
            title: label || id,
            format: body.find(cell => cell.id === id)?.format,
        }))
}

function createExportPayload(
    resolvedURL: string,
    format: string,
    charset: string,
    fields: Array<{ id: string, title: string, format?: string }>,
    filename?: string,
) {
    return {
        format,
        charset,
        url: resolvedURL,
        fields,
        filename,
    }
}

interface InheritedExtraParams {
    [INHERITED_SOURCE_FIELD_ID]?: string
    [PARENT_ROW_ID]?: string | number
    [SORTING]?: Record<string, unknown>
}

/** @OINFO в зависимости от value | link возвращает
 * 1. const value если оно передано
 * 2. Значение в model */
function* getSetting(valueMeta: LinkProps) {
    const state: State = yield select()

    return linkResolver(state, valueMeta)
}

/**
 * Извлекает имя файла из заголовка Content-Disposition
 * Обрабатывает оба формата: filename="..." и filename*=utf-8''...
 */
function extractFilenameFromContentDisposition(contentDisposition: string | null): string | null {
    if (!contentDisposition) { return null }

    // 1. Современный стандарт RFC 5987
    const filenameStartMatch = contentDisposition.match(/filename\*=utf-8''([^;]+)/i)

    if (filenameStartMatch) {
        try {
            return decodeURIComponent(filenameStartMatch[1].trim())
        } catch (e) {
            console.error('Failed to decode filename*:', e)
        }
    }

    // 2. Fallback на обычный filename (для старых серверов или ASCII имен)
    const filenameMatch = contentDisposition.match(/filename="?([^\n";]+)"?/i)

    if (filenameMatch) {
        const filename = filenameMatch[1].trim()

        // Пропускаем MIME-encoded варианты, они некорректны для HTTP
        if (!filename.startsWith('=?')) {
            return filename
        }
    }

    return null
}

export function* effect({ payload }: Action<string, Payload>) {
    const { exportDatasource, values, baseURL, widgetId, allLimit = 1000, filename } = payload

    if (!exportDatasource || !values || !baseURL || !widgetId) {
        logger.error(ATTRIBUTES_ERROR)
        yield cancel()
    }

    const { format: metaFormat, charset: metaCharset, type: metaType } = values

    const format: string = yield getSetting(metaFormat)
    const charset: string = yield getSetting(metaCharset)
    const type: string = yield getSetting(metaType)

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
        inheritedProvider as object || provider as object,
        {
            size: type === 'page' ? paging.size : allLimit,
            page: type === 'page' ? paging.page : 1,
            sorting,
            ...extraParams,
        },
    )

    const cells: { header: HeaderCell[], body: BodyCell[] } = yield select(getTableCells(widgetId))

    const columns = getAllValuesByKey(cells.header, { keyToIterate: 'children' })?.filter(obj => !NON_EXPORTABLE_KEYS.some(key => key in obj))

    const fields = getShowedColumnsWithLabels(columns, cells.body)
    const exportPayload = createExportPayload(resolvedURL, format, charset, fields, filename)

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

            const contentDisposition = response.headers.get('Content-Disposition')
            const fileName = extractFilenameFromContentDisposition(contentDisposition)

            a.style.display = 'none'
            a.href = url
            a.download = fileName || `document.${format}`
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
