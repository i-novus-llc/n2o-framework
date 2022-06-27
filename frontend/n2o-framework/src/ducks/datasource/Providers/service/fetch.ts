import {
    call,
    fork,
    cancel,
} from 'redux-saga/effects'
import isEqual from 'lodash/isEqual'
import type { Task } from 'redux-saga'

// @ts-ignore ignore import error from js file
import { FETCH_WIDGET_DATA } from '../../../../core/api'
// @ts-ignore ignore import error from js file
import fetchSaga from '../../../../sagas/fetch'
import { REQUEST_CACHE_TIMEOUT } from '../../../../constants/time'

type RequestInfo<TData> = {
    request: Promise<TData>
    provider: DataProvider
    worker: Task
    timer?: number
}

/**
 * @typedef {Object} CachedRequest
 * @property {Promise} request
 * @property {Object} provider
 * @property {Object} worker
 * @property {Number} timer
 */
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
 */
export function* fetch<TData = unknown>(datasource: string, provider: DataProvider) {
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
    const worker: Task = yield fork(fetchSaga, FETCH_WIDGET_DATA, {
        basePath,
        baseQuery,
        headers: headersParams,
    })
    const request: Promise<TData> = worker.toPromise()
    const requestInfo: RequestInfo<TData> = { request, provider, worker }

    request.then(() => {
        if (worker.isCancelled()) {
            return /* Promise.reject(new Error('Abort')) */
        }
        requestInfo.timer = setTimeout(() => {
            delete requestMap[datasource]
        }, REQUEST_CACHE_TIMEOUT)
    }, () => {
        delete requestMap[datasource]
    })
    requestMap[datasource] = requestInfo

    return (yield call(() => request)) as Promise<TData>
}
