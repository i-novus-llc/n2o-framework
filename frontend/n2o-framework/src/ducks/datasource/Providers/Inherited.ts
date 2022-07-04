import { select } from 'redux-saga/effects'
import { get, cloneDeep } from 'lodash'

import { dataSourceByIdSelector } from '../selectors'
import type { QueryOptions, InheritedProvider } from '../Provider'
import type { DataSourceState } from '../DataSource'
import { makeGetModelByPrefixSelector } from '../../models/selectors'

import { applyFilter } from './storage/applyFilter'
import { applySorting } from './storage/applySorting'
import { applyPaging } from './storage/applyPaging'

export function* submit() {
    // TODO NNO-8034
}

export function* query(id: string, {
    sourceDs: datasourceId,
    sourceModel: prefix,
    sourceField,
}: InheritedProvider, options: QueryOptions) {
    const datasource: DataSourceState = yield select(dataSourceByIdSelector(id))
    const { size, sorting, page } = datasource

    const model: object | void = yield select(makeGetModelByPrefixSelector(prefix, datasourceId))
    const data = cloneDeep(sourceField ? get(model, sourceField) : model)

    if (!data) {
        return {
            list: [],
            page: 1,
            count: 0,
        }
    }

    const filtered = applyFilter(Array.isArray(data) ? data : [data])
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
