import {
    put,
    select,
} from 'redux-saga/effects'
import { isEqual } from 'lodash'

import { setModel } from '../../models/store'
// @ts-ignore ignore import error from js file
import { generateErrorMeta } from '../../../utils/generateErrorMeta'
import { id as generateId } from '../../../utils/id'
import { ModelPrefix } from '../../../core/datasource/const'
import { dataSourceByIdSelector } from '../selectors'
import {
    rejectRequest,
    resolveRequest,
} from '../store'
import { makeGetModelByPrefixSelector } from '../../models/selectors'
import type { IProvider, QueryResult } from '../Provider'
import { ProviderType } from '../Provider'
import { query as serviceQuery } from '../Providers/Service'
import type { DataRequestAction } from '../Actions'
import type { DataSourceState } from '../DataSource'

type Query<TProvider extends IProvider> = (id: string, provider: TProvider, options: unknown) => unknown

function getQuery<
    TProvider extends IProvider,
    TProviderType extends ProviderType = TProvider['type']
>(provider: TProviderType): Query<TProvider> {
    switch (provider) {
        case ProviderType.service: { return serviceQuery as Query<IProvider> }
        case ProviderType.storage:
        case ProviderType.inherited:
        case ProviderType.application:
        default: { return () => { throw new Error(`hasn't implementation for provider type: "${provider}`) } }
    }
}

export function* dataRequest({ payload }: DataRequestAction) {
    const { id, options } = payload

    try {
        const { provider, components }: DataSourceState = yield select(dataSourceByIdSelector(id))

        if (!provider) {
            throw new Error('Can\'t request data with empty provider')
        }
        if (!components.length) {
            throw new Error('Unnecessary request for datasource with empty components list ')
        }

        const query = getQuery(provider.type)

        const response: QueryResult = yield query(id, provider, options)

        const oldData = (yield select(makeGetModelByPrefixSelector(ModelPrefix.source, id))) as object[]

        // фикс, чтобы компонентам долетало, что данные обновились
        if (isEqual(oldData, response.list)) {
            yield put(setModel(ModelPrefix.source, id, []))
        }

        yield put(setModel(ModelPrefix.source, id, response.list))

        yield put(resolveRequest(id, response))
    } catch (error) {
        const err = error as { message: string, stack: string, json?: { meta: unknown} }

        // eslint-disable-next-line no-console
        console.warn(`JS Error: DataSource(${id}) fetch saga. ${err.message}`)
        yield put(
            rejectRequest(
                id,
                error,
                err.json?.meta ||
                {
                    meta: generateErrorMeta({
                        id: generateId(),
                        text: 'Произошла внутренняя ошибка',
                        stacktrace: err.stack,
                        closeButton: true,
                    }),
                },
            ),
        )
    }
}
