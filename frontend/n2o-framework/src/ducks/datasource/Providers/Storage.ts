import { select } from 'redux-saga/effects'

import { dataSourceByIdSelector } from '../selectors'
import type { QueryOptions, StorageProvider, StorageSubmit } from '../Provider'
import { StorageType } from '../Provider'
import type { DataSourceState } from '../DataSource'
import { makeGetModelByPrefixSelector } from '../../models/selectors'

import { applyFilter } from './storage/applyFilter'
import { applySorting } from './storage/applySorting'
import { applyPaging } from './storage/applyPaging'

export function* submit(id: string, { key, model: prefix, storage }: StorageSubmit) {
    const model: unknown = yield select(makeGetModelByPrefixSelector(prefix, id))
    let data

    if (Array.isArray(model)) {
        data = model
    } else if (model) {
        data = [model]
    }

    const stringData = JSON.stringify(data)

    if (storage === StorageType.local) {
        localStorage.setItem(key, stringData)
    } else {
        sessionStorage.setItem(key, stringData)
    }
}

export function* query(id: string, { storage, key }: StorageProvider, options: QueryOptions) {
    const datasource: DataSourceState = yield select(dataSourceByIdSelector(id))
    const { size, sorting, page } = datasource

    if (!key) {
        throw new Error('Parametr "key" is required for query data')
    }

    const storageData = storage === StorageType.local
        ? localStorage.getItem(key)
        : sessionStorage.getItem(key)

    if (!storageData) {
        return {
            list: [],
            page: 1,
            count: 0,
        }
    }

    const json = JSON.parse(storageData)

    if (!Array.isArray(json)) {
        throw new Error('invalid data format')
    }

    const filtered = applyFilter(json)
    const sortered = applySorting(filtered, sorting)
    const { list, page: newPage } = applyPaging(
        sortered,
        {
            size,
            page: typeof options.page === 'undefined' ? page : options.page,
        },
    )

    return {
        list,
        page: newPage,
        count: filtered.length,
    }
}
