import { put, select } from 'redux-saga/effects'
import { get, cloneDeep, set } from 'lodash'

import { dataSourceByIdSelector } from '../selectors'
import type { QueryOptions, InheritedProvider, InheritedSubmit } from '../Provider'
import { getModelByPrefixAndNameSelector } from '../../models/selectors'
import { setModel, removeModel } from '../../models/store'
import evalExpression from '../../../utils/evalExpression'
import { State } from '../../State'
import { DataSourceState } from '../DataSource'

import { applyFilter } from './storage/applyFilter'
import { applySorting } from './storage/applySorting'
import { applyPaging } from './storage/applyPaging'

export function* submit(id: string, {
    model: prefix,
    targetDs: targetId,
    targetModel: targetPrefix,
    targetField,
    submitValueExpression,
}: InheritedSubmit) {
    const sourceModel: object = yield select(getModelByPrefixAndNameSelector(prefix, id))
    const targetModel: object = yield select(getModelByPrefixAndNameSelector(prefix, targetId))
    let source: void | object = sourceModel

    if (submitValueExpression) {
        const target = targetField ? get(targetModel, targetField) : targetModel

        source = evalExpression<object | void>(submitValueExpression, {
            source: sourceModel,
            target,
        })
    }

    let resultModel = source

    if (targetField) {
        resultModel = cloneDeep(targetModel) || {}
        set(resultModel, targetField, source)
    }

    if (resultModel) {
        yield put(setModel(targetPrefix, targetId, resultModel))
    } else {
        yield put(removeModel(targetPrefix, targetId))
    }
}

export function* query(id: string, {
    sourceDs: datasourceId,
    sourceModel: prefix,
    sourceField,
    fetchValueExpression,
    filters,
}: InheritedProvider, options: QueryOptions) {
    const datasource: DataSourceState = yield select(dataSourceByIdSelector(id))
    const { sorting, paging: { size, page } } = datasource

    const sourceModel: object | void = yield select(getModelByPrefixAndNameSelector(prefix, datasourceId))
    const sourceData = cloneDeep(sourceField ? get(sourceModel, sourceField) : sourceModel)

    if (!sourceData) {
        return {
            list: [],
            paging: {
                page: 1,
                count: 0,
            },
        }
    }

    let sourceList = Array.isArray(sourceData) ? sourceData : [sourceData]

    if (fetchValueExpression) {
        const normalized = evalExpression(fetchValueExpression, { source: sourceData })

        if (!Array.isArray(normalized)) {
            throw new Error('Ошибка нормализации данных')
        }

        sourceList = normalized
    }

    const state: State = yield select()
    const filtered: object[] = applyFilter(
        state,
        sourceList,
        filters,
    )
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
