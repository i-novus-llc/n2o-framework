import { select, put } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'

import { dataSourceByIdSelector, dataSourceProviderSelector } from '../selectors'
import type { Provider, QueryOptions, StorageProvider, StorageSubmit } from '../Provider'
import { ProviderType } from '../Provider'
import type { DataSourceState } from '../DataSource'
import { getModelByPrefixAndNameSelector } from '../../models/selectors'
import { State } from '../../State'
import { dataRequest } from '../store'
import { getStorage } from '../../../utils/Storage'

import { applyFilter } from './storage/applyFilter'
import { applySorting } from './storage/applySorting'
import { applyPaging } from './storage/applyPaging'

const KEY_PREFIX = 'n2o/datasource/'

export const getFullKey = (key: string) => `${KEY_PREFIX}${key}`

export function* submit(id: string, { key, model: prefix, storage: storageType }: StorageSubmit) {
    const model: unknown = yield select(getModelByPrefixAndNameSelector(prefix, id))
    const storage = getStorage(storageType)

    if (isEmpty(model)) {
        return storage.removeItem(getFullKey(key))
    }

    const data = Array.isArray(model) ? model : [model]

    /** @INFO Чтобы пересчитался count в datasource */
    yield put(dataRequest(id))

    return storage.setItem(getFullKey(key), data)
}

export function* query(id: string, { storage: storageType, key }: StorageProvider, options: QueryOptions) {
    const datasource: DataSourceState = yield select(dataSourceByIdSelector(id))
    const { sorting, paging: { page, size } } = datasource

    if (!key) { throw new Error('Parameter "key" is required for query data') }

    const storageData = getStorage(storageType).getItem<object[]>(getFullKey(key))

    if (!storageData) {
        return {
            list: [],
            paging: {
                page: 1,
                count: 0,
            },
        }
    }

    if (!Array.isArray(storageData)) { throw new Error('invalid data format') }

    const state: State = yield select()
    const filtered = applyFilter(state, storageData)
    const sorted = applySorting(filtered, sorting)
    const { list, paging } = applyPaging(
        sorted,
        {
            size,
            page: typeof options.page === 'undefined' ? page : options.page,
        },
    )

    return {
        list,
        paging: {
            page: paging.page,
            count: filtered.length,
        },
    }
}

export function* clear({ meta }: {
    meta: { clear?: string }
}) {
    const { clear: id } = meta

    if (!id) { return }

    const provider: Provider | void = yield select(dataSourceProviderSelector(id))

    if (!provider || provider.type !== ProviderType.storage) { return }

    const { storage: storageType, key } = provider as StorageProvider

    getStorage(storageType).removeItem(getFullKey(key))
}
