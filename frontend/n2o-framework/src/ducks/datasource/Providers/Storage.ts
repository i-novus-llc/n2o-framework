import { select } from 'redux-saga/effects'
import { isNil } from 'lodash'

import { dataSourceByIdSelector, dataSourceProviderSelector } from '../selectors'
import type { IProvider, QueryOptions, StorageProvider, StorageSubmit } from '../Provider'
import { ProviderType, StorageType } from '../Provider'
import type { DataSourceState } from '../DataSource'
import { getModelByPrefixAndNameSelector } from '../../models/selectors'
import { State } from '../../State'

import { applyFilter } from './storage/applyFilter'
import { applySorting } from './storage/applySorting'
import { applyPaging } from './storage/applyPaging'

const KEY_PREFIX = 'n2o/datasource/'

const getFullKey = (key: string) => `${KEY_PREFIX}${key}`

export function* submit(id: string, { key, model: prefix, storage: storageType }: StorageSubmit) {
    const model: unknown = yield select(getModelByPrefixAndNameSelector(prefix, id))
    const storage = storageType === StorageType.local ? localStorage : sessionStorage

    if (isNil(model)) {
        return storage.removeItem(getFullKey(key))
    }

    const data = Array.isArray(model) ? model : [model]
    const stringData = JSON.stringify(data)

    return storage.setItem(getFullKey(key), stringData)
}

export function* query(id: string, { storage: storageType, key }: StorageProvider, options: QueryOptions) {
    const datasource: DataSourceState = yield select(dataSourceByIdSelector(id))
    const { sorting, paging: { page, size } } = datasource

    if (!key) {
        throw new Error('Parametr "key" is required for query data')
    }

    const storage = storageType === StorageType.local ? localStorage : sessionStorage
    const storageData = storage.getItem(getFullKey(key))

    if (!storageData) {
        return {
            list: [],
            paging: {
                page: 1,
                count: 0,
            },
        }
    }

    const json = JSON.parse(storageData)

    if (!Array.isArray(json)) {
        throw new Error('invalid data format')
    }

    const state: State = yield select()
    const filtered: object[] = applyFilter(state, json)
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
    meta: {
        clear?: string
    }
}) {
    const { clear: id } = meta

    if (!id) { return }

    const provider: IProvider | void = yield select(dataSourceProviderSelector(id))

    if (!provider || provider.type !== ProviderType.storage) {
        return
    }

    const { storage: storageType, key } = provider as StorageProvider
    const storage = storageType === StorageType.local ? localStorage : sessionStorage

    storage.removeItem(getFullKey(key))
}
