import { createAction } from '@reduxjs/toolkit'
import { cancel, select, takeEvery } from 'redux-saga/effects'

import { dataProviderResolver } from '../../core/dataProviderResolver'
import { dataSourceByIdSelector } from '../datasource/selectors'
import { getTableHeaderCells } from '../table/selectors'
import { Action } from '../Action'
import { getModelSelector } from '../models/selectors'
import { ModelPrefix } from '../../core/datasource/const'
import { DataSourceState } from '../datasource/DataSource'
import { State } from '../State'
import { Provider, ProviderType } from '../datasource/Provider'
import { HeaderCell } from '../table/Table'
import { getAllValuesByKey } from '../../components/Table/utils'

import { UTILS_PREFIX } from './constants'
import { EffectWrapper } from './utils/effectWrapper'
import { escapeUrl } from './utils/escapeUrl'

const ATTRIBUTES_ERROR = 'Ошибка экспорта, payload содержит не все параметры'
const PARAMS_ERROR = 'Ошибка экспорта, не передан формат или кодировка'
const SHOW = 'show'
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

function getShowedColumns(columns: HeaderCell[]): string[] {
    return columns
        .filter(column => column.visible && column.visibleState)
        .map(column => column.id)
}

function createExportUrl(
    resolvedURL: string,
    baseURL: string,
    format: string,
    charset: string,
    showed: string[],
) {
    const { pathname } = window.location

    const path = pathname.slice(0, -1)
    const exportURL = `${path}${baseURL}?format=${format}&charset=${charset}&url=`

    if (!showed.length) {
        return `${exportURL}${escapeUrl(resolvedURL)}`
    }

    let url = resolvedURL

    for (const show of showed) {
        url += `&${SHOW}=${show}`
    }

    return `${exportURL}${escapeUrl(url)}`
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

    const headerCells: HeaderCell[] = yield select(getTableHeaderCells(widgetId))
    const columns = getAllValuesByKey(headerCells, { keyToIterate: 'children' })
    const showed = getShowedColumns(columns)
    const exportURL = createExportUrl(resolvedURL, baseURL, format, charset, showed)

    window.open(exportURL, '_blank')
}

// @ts-ignore проблема с типизацией saga
export const sagas = [takeEvery(creator.type, EffectWrapper(effect))]
