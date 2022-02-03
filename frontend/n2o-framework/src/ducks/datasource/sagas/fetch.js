import {
    call,
    fork,
    cancel,
} from 'redux-saga/effects'
import isEqual from 'lodash/isEqual'

import { FETCH_WIDGET_DATA } from '../../../core/api'
import fetchSaga from '../../../sagas/fetch'
import { REQUEST_CACHE_TIMEOUT } from '../../../constants/time'

/**
 * @typedef {Object} CachedRequest
 * @property {Promise} request
 * @property {Object} provider
 * @property {Object} worker
 * @property {Number} timer
 */
const requestMap = Object.create(null)

/**
 * Обёртка над запросом за данными.
 * Нужен для предотвращения параллельных (плюс небольшая задержка = REQUEST_CACHE_TIMEOUT) запросов с одинаковыми
 * параметрами от нескольких виджетов, использующих одну и ту же модель (datasource)
 * @param {string} datasource
 * @param {{ basePath: string, baseQuery: string , headersParams: object }} provider
 */
export function* fetch(datasource, provider) {
    /** @type {CachedRequest} */
    const cached = requestMap[datasource]

    if (cached) {
        // Если новый запрос идентичен текущему, возвращаем результат текущего
        if (isEqual(provider, cached.provider)) {
            return yield call(() => Promise.reject(new Error('cancel duplicate request')))
        }

        // Если новые фильтры, то текущий запрос уже не актуален - отменяем его
        clearTimeout(cached.timer)
        yield cancel(cached.worker)
    }

    const { basePath, baseQuery, headersParams } = provider
    const worker = (yield fork(fetchSaga, FETCH_WIDGET_DATA, {
        basePath,
        baseQuery,
        headers: headersParams,
    }))
    const request = worker.toPromise()
    let timer

    request.then(() => {
        if (worker.isCancelled()) {
            return /* Promise.reject(new Error('Abort')) */
        }
        timer = setTimeout(() => {
            delete requestMap[datasource]
        }, REQUEST_CACHE_TIMEOUT)
    }, () => {
        delete requestMap[datasource]
    })
    requestMap[datasource] = { request, provider, worker, timer }

    return yield call(() => request)
}
