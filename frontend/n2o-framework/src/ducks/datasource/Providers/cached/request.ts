import get from 'lodash/get'
import isEqual from 'lodash/isEqual'
import { select } from 'redux-saga/effects'
import dayjs from 'dayjs'

import type { State as GlobalState } from '../../../State'
import { dataProviderResolver } from '../../../../core/dataProviderResolver'
import { CachedProvider, QueryResult } from '../../Provider'
import { getFullKey } from '../Storage'
import { fetch } from '../service/fetch'
import { clearEmptyParams } from '../../../../utils/clearEmptyParams'

import { checkExpiration } from './checkExpiration'
import { CacheData } from './types'

interface Params {
    provider: CachedProvider
    page: number
    sorting: string
    id: string
    apiProvider: unknown
    storage: Storage
    key: string
}

export function* request(params: Params) {
    const { provider, page, sorting, id, apiProvider, storage, key } = params

    const { size } = provider
    const query = { page: get(params, 'page', page), size, sorting }
    const state: GlobalState = yield select()

    let resolvedProvider = null

    try {
        resolvedProvider = dataProviderResolver(state, provider, query)
    } catch (error) {
        // TODO обработка error?

        // eslint-disable-next-line no-console
        console.error(error)

        return { list: [], paging: { count: 0, page: 1 } }
    }

    const mappings = clearEmptyParams({
        baseQuery: resolvedProvider.baseQuery,
        pathParams: resolvedProvider.pathParams,
    })

    const storageData = storage.getItem(getFullKey(key))

    if (storageData) {
        const { cacheExpires } = provider

        const json: CacheData = JSON.parse(storageData)
        const { timestamp } = json
        const isExpired = checkExpiration(timestamp, cacheExpires)

        if (!isExpired) {
            const { mappings: cachedMappings } = json

            if (isEqual(cachedMappings, mappings)) { return json }
        }
    }

    const data: QueryResult = yield fetch(id, resolvedProvider, apiProvider)
    const cachedData: CacheData = {
        ...data,
        timestamp: dayjs().format(),
        mappings,
    }

    storage.setItem(getFullKey(key), JSON.stringify(cachedData))

    return data
}
