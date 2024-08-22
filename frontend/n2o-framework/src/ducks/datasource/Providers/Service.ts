import { call, select, delay } from 'redux-saga/effects'
import get from 'lodash/get'

// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../../../core/dataProviderResolver'
import { dataSourceByIdSelector } from '../selectors'
import type { QueryOptions, QueryResult, ServiceProvider, ServiceSubmit } from '../Provider'
import type { State as GlobalState } from '../../State'
import type { DataSourceState } from '../DataSource'
import { ModelPrefix } from '../../../core/datasource/const'
import { mapQueryToUrl } from '../../pages/sagas/restoreFilters'
import { HandleInvokePayload, handleInvoke } from '../../../sagas/actionsImpl'

import { fetch } from './service/fetch'

export function* submit(id: string, provider: ServiceSubmit, apiProvider: unknown) {
    const { pageId }: DataSourceState = yield select(dataSourceByIdSelector(id))
    const action: { payload: HandleInvokePayload } = {
        payload: {
            datasource: id,
            dataProvider: provider,
            model: ModelPrefix.active,
            pageId: pageId || '',
        },
    }

    yield call(handleInvoke, apiProvider, action)
}

export function* invoke() {
    // TODO Перенести сюда инвок из actionsImpl
}

export function* query(id: string, provider: ServiceProvider, options: QueryOptions, apiProvider: unknown) {
    const {
        sorting,
        paging: { page, size, withCount },
        pageId,
    }: DataSourceState = yield select(dataSourceByIdSelector(id))

    if (!provider.url) {
        throw new Error('Parameter "url" is required for fetch data')
    }

    // Редакс состояние не успевает обновиться после маппинга из урла,
    // запрос за данными слишком быстро запускается
    yield delay(16)
    yield call(mapQueryToUrl, pageId || '')

    const query = {
        page: get(options, 'page', page),
        size,
        sorting,
        withCount: get(options, 'withCount', withCount),
    }

    const state: GlobalState = yield select()

    let resolvedProvider = null

    try {
        resolvedProvider = dataProviderResolver(
            state,
            provider,
            query,
            options,
        )
    } catch (error) {
        /*
         * Сброс списка при отсутствии обязательных полей датапровайдера
         * нужно чтобы при чистки master-ds чистился и child-ds.
         * В этом случае dataProviderResolver кинет error
         */

        return { list: [], paging: { count: 0, page: 1 } }
    }

    return (yield fetch(id, resolvedProvider, apiProvider)) as QueryResult
}
