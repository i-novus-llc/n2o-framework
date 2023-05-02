import { createAction } from '@reduxjs/toolkit'
import { select, takeEvery, cancel } from 'redux-saga/effects'

// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { dataSourceByIdSelector } from '../datasource/selectors'
import { Action } from '../Action'
import { getModelSelector } from '../models/selectors'
import { ModelPrefix } from '../../core/datasource/const'
import { DataSourceState } from '../datasource/DataSource'
import { State } from '../State'

import { UTILS_PREFIX } from './constants'
import { EffectWrapper } from './utils/effectWrapper'
import { escapeUrl } from './utils/escapeUrl'

const ATTRIBUTES_ERROR = 'Ошибка экспорта attributes содержит не все параметры'
const PARAMS_ERROR = 'Ошибка экспорта не передан формат или кодировка'

export type Payload = {
    'exportDatasource': string,
    'configDatasource': string,
    'baseURL': string
}

export const creator = createAction(
    `${UTILS_PREFIX}export`,
    (payload: Payload, meta: object) => ({
        payload,
        meta,
    }),
)

export function* effect({ payload }: Action<string, Payload>) {
    const { exportDatasource, configDatasource, baseURL } = payload

    if (!exportDatasource || !configDatasource || !baseURL) {
        // eslint-disable-next-line no-console
        console.error(ATTRIBUTES_ERROR)

        yield cancel()
    }

    const modelLink = `models.${ModelPrefix.active}.${configDatasource}`
    const model: { format: string, charset: string } = yield select(getModelSelector(modelLink))
    const { format, charset } = model

    if (!format || !charset) {
        // eslint-disable-next-line no-console
        console.error(PARAMS_ERROR)

        yield cancel()
    }

    const state: State = yield select()

    const dataSource: DataSourceState = yield select(dataSourceByIdSelector(exportDatasource))
    const { provider, paging = {}, sorting = {} } = dataSource
    const { url } = dataProviderResolver(state, provider, { ...paging, sorting })
    const { pathname } = window.location

    const escapedUrl = escapeUrl(url)
    const path = pathname.slice(0, -1)

    const exportUrl = `${path}${baseURL}?format=${format}&charset=${charset}&url=${escapedUrl}`

    window.open(exportUrl, '_blank')
}

// @ts-ignore Проблемы с типизацией saga
export const sagas = [takeEvery(creator.type, EffectWrapper(effect))]
