import { put, select } from 'redux-saga/effects'
import { get, cloneDeep, set } from 'lodash'

import { dataSourceByIdSelector } from '../selectors'
import type { QueryOptions, InheritedProvider, InheritedSubmit } from '../Provider'
import type { DataSourceState } from '../DataSource'
import { makeGetModelByPrefixSelector } from '../../models/selectors'
import { setModel, removeModel } from '../../models/store'
import evalExpression from '../../../utils/evalExpression'

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
    const sourceModel = cloneDeep((yield select(makeGetModelByPrefixSelector(prefix, id))) as object)
    const targetModel = cloneDeep((yield select(makeGetModelByPrefixSelector(prefix, id))) as object | void)
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
}: InheritedProvider, options: QueryOptions) {
    const datasource: DataSourceState = yield select(dataSourceByIdSelector(id))
    const { sorting, paging: { size, page } } = datasource

    const model: object | void = yield select(makeGetModelByPrefixSelector(prefix, datasourceId))
    const data = cloneDeep(sourceField ? get(model, sourceField) : model)

    if (!data) {
        return {
            list: [],
            paging: {
                page: 1,
                count: 0,
            },
        }
    }

    const normalized = fetchValueExpression ? evalExpression(fetchValueExpression, { source: data }) : data

    if (!Array.isArray(normalized)) {
        throw new Error('Ошибка нормализации данных')
    }

    const filtered = applyFilter(Array.isArray(normalized) ? normalized : [normalized])
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
