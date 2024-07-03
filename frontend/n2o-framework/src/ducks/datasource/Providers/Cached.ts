import { select } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { dataSourceByIdSelector } from '../selectors'
import { CachedProvider, CachedSubmit, QueryOptions, type QueryResult, StorageType } from '../Provider'
import { getModelByPrefixAndNameSelector } from '../../models/selectors'

import { getFullKey } from './Storage'
import { cachedRequest } from './service/cachedRequest'

export function* submit(id: string, provider: CachedSubmit) {
    const {
        auto,
        clearCache,
        key,
        model: prefix,
        storage: storageType,
    } = provider

    const model: Record<string, unknown> = yield select(getModelByPrefixAndNameSelector(prefix, id))
    const storage = storageType === StorageType.local ? localStorage : sessionStorage
    const fullKey = getFullKey(key)

    if (isEmpty(model) || (clearCache && !auto)) { return storage.removeItem(fullKey) }

    const storageData = storage.getItem(fullKey) || '{}'

    const list = Array.isArray(model) ? model : [model]
    const stringData = JSON.stringify({ ...JSON.parse(storageData), list })

    return storage.setItem(fullKey, stringData)
}

export function* query(id: string, provider: CachedProvider, options: QueryOptions, apiProvider: unknown) {
    if (!provider.url) { throw new Error('Parameter "url" is required for fetch data') }

    const { sorting, paging: { page } } = yield select(dataSourceByIdSelector(id))

    const { key, storage: storageType } = provider

    const storage = storageType === StorageType.local ? localStorage : sessionStorage
    const params = { provider, page, sorting, id, apiProvider, storage, key }

    return (yield cachedRequest(params)) as QueryResult
}
