import { call, select } from 'redux-saga/effects'

import { dataSourceByIdSelector } from '../selectors'
import { CachedAutoSubmit, CachedProvider, CachedSubmit, ProviderType, QueryOptions, type QueryResult, StorageType } from '../Provider'
import { getModelByPrefixAndNameSelector, Model } from '../../models/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

import { getFullKey } from './Storage'
import { request } from './cached/request'
import { CacheData } from './cached/types'
import { submit as serviceSubmit } from './Service'

export function* submit(
    id: string,
    { clearCache, storage: storageType, key, ...provider }: CachedSubmit,
    apiProvider: unknown,
) {
    yield call(serviceSubmit, id, {
        ...provider,
        type: ProviderType.service,
    }, apiProvider)

    if (clearCache) {
        const fullKey = getFullKey(key)
        const storage = storageType === StorageType.local ? localStorage : sessionStorage

        storage.removeItem(fullKey)
    }
}

export function* autoSubmit(id: string, provider: CachedAutoSubmit) {
    const {
        key,
        model: prefix,
        storage: storageType,
    } = provider

    const model: Model | Model[] = yield select(getModelByPrefixAndNameSelector(prefix, id))
    const storage = storageType === StorageType.local ? localStorage : sessionStorage
    const fullKey = getFullKey(key)
    const storageData: CacheData = JSON.parse(storage.getItem(fullKey) || 'null') || {
        list: [],
        mappings: {},
        paging: {
            page: 0,
            size: 0,
            count: 0,
        },
        timestamp: Date.now(),
    }

    let list: Model[] = []

    if (prefix === ModelPrefix.source) {
        list = model as Model[]
    } else {
        // storageData.active = model as Model
        list = storageData.list.length <= 1
            ? [model as Model]
            : storageData.list
    }

    storageData.list = list
    storageData.paging.size = list.length
    storageData.paging.count = list.length
    storageData.paging.page = 1
    storageData.paging.size = storageData.paging.size >= list.length ? storageData.paging.size : list.length

    return storage.setItem(fullKey, JSON.stringify(storageData))
}

export function* query(id: string, provider: CachedProvider, options: QueryOptions, apiProvider: unknown) {
    if (!provider.url) { throw new Error('Parameter "url" is required for fetch data') }

    const { sorting, paging: { page } } = yield select(dataSourceByIdSelector(id))

    const { key, storage: storageType } = provider

    const storage = storageType === StorageType.local ? localStorage : sessionStorage
    const params = { provider, page, sorting, id, apiProvider, storage, key }

    return (yield request(params)) as QueryResult
}
