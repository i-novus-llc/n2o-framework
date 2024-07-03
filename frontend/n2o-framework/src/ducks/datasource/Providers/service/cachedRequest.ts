import get from 'lodash/get'
import { select } from 'redux-saga/effects'
import moment from 'moment'

import type { State as GlobalState } from '../../../State'
import { dataProviderResolver } from '../../../../core/dataProviderResolver'
import { CachedProvider, QueryResult } from '../../Provider'
import { getFullKey } from '../Storage'

import { checkExpiration } from './checkExpiration'
import { fetch } from './fetch'
import { createCachedMappings, checkInvalidateParams, Key, Mapping } from './cachedMappings'

interface Params {
    provider: CachedProvider
    page: number
    sorting: string
    id: string
    apiProvider: unknown
    storage: Storage
    key: string
}

export function* cachedRequest(params: Params) {
    const { provider, page, sorting, id, apiProvider, storage, key } = params

    const { size, invalidateParams } = provider
    const query = { page: get(params, 'page', page), size, sorting }
    const state: GlobalState = yield select()

    let resolvedProvider = null

    try {
        resolvedProvider = dataProviderResolver(state, provider, query, params)
    } catch (error) {
        // TODO обработка error?

        // eslint-disable-next-line no-console
        console.error(error)

        return { list: [], paging: { count: 0, page: 1 } }
    }

    const { baseQuery, pathParams }: { baseQuery: Mapping, pathParams: Mapping } = resolvedProvider
    const mappings = { [Key.QUERY]: { ...baseQuery }, [Key.PATH]: { ...pathParams } }

    const storageData = storage.getItem(getFullKey(key))

    if (storageData) {
        const { cacheExpires } = provider

        const json = JSON.parse(storageData)
        const { timestamp } = json
        const isExpired = checkExpiration(timestamp, cacheExpires)

        if (!isExpired) {
            const { cachedMappings } = json

            const isValid = checkInvalidateParams(
                invalidateParams,
                cachedMappings,
                mappings,
            )

            if (isValid) { return json }
        }
    }

    const data: QueryResult = yield fetch(id, resolvedProvider, apiProvider)
    const cachedData = { ...data }

    cachedData.timestamp = moment().format()
    cachedData.cachedMappings = createCachedMappings(invalidateParams, mappings)

    storage.setItem(getFullKey(key), JSON.stringify(cachedData))

    return data
}
