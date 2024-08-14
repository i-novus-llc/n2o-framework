import {
    call,
    fork,
    cancel,
} from 'redux-saga/effects'
import isEqual from 'lodash/isEqual'
import type { Task } from 'redux-saga'

import { FETCH_WIDGET_DATA } from '../../../../core/api'
import fetchSaga from '../../../../sagas/fetch'
import { REQUEST_CACHE_TIMEOUT } from '../../../../constants/time'

type RequestInfo<TData> = {
    request: Promise<TData>
    provider: DataProvider
    worker: Task
    timer?: number
}

const requestMap: Record<string, RequestInfo<unknown>> = Object.create(null)

export interface DataProvider {
    basePath: string
    baseQuery: string
    headersParams: object
}

/**
 * Обёртка над запросом за данными.
 * Нужен для предотвращения параллельных (плюс небольшая задержка = REQUEST_CACHE_TIMEOUT) запросов с одинаковыми
 * параметрами от нескольких виджетов, использующих одну и ту же модель (datasource)
 * @param {string} datasource
 * @param {{ basePath: string, baseQuery: string , headersParams: object }} provider
 * @param apiProvider
 */
export function* fetch<TData = unknown>(datasource: string, provider: DataProvider, apiProvider: unknown) {
    const cached = requestMap[datasource] as RequestInfo<TData> | void

    if (cached) {
        // Если новый запрос идентичен текущему, возвращаем результат текущего
        if (isEqual(provider, cached.provider)) {
            return (yield call(() => Promise.reject(new Error('cancel duplicate request')))) as Promise<never>
        }

        // Если новые фильтры, то текущий запрос уже не актуален - отменяем его
        clearTimeout(cached.timer)
        yield cancel(cached.worker)
    }

    const { basePath, baseQuery, headersParams } = provider
    // @ts-ignore import from js file
    const worker: Task = yield fork(fetchSaga, FETCH_WIDGET_DATA, { basePath, baseQuery, headers: headersParams }, apiProvider)
    const request: Promise<TData> = worker.toPromise()
    const requestInfo: RequestInfo<TData> = { request, provider, worker }

    request.then(() => {
        if (worker.isCancelled()) {
            delete requestMap[datasource]

            return /* Promise.reject(new Error('Abort')) */
        }
        // @ts-ignore import from js file
        requestInfo.timer = setTimeout(() => {
            delete requestMap[datasource]
        }, REQUEST_CACHE_TIMEOUT)
    }, () => {
        delete requestMap[datasource]
    })
    requestMap[datasource] = requestInfo

    return (yield call(() => request)) as Promise<TData>
}
