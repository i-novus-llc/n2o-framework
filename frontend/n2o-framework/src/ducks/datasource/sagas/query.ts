import { put, select } from 'redux-saga/effects'

import { setModel } from '../../models/store'
// @ts-ignore ignore import error from js file
import { generateErrorMeta } from '../../../utils/generateErrorMeta'
import { id as generateId } from '../../../utils/id'
import { ModelPrefix } from '../../../core/datasource/const'
import { IMeta, IValidationFieldMessage } from '../../../sagas/types'
import { dataSourceByIdSelector } from '../selectors'
import {
    failValidate,
    rejectRequest,
    resolveRequest,
} from '../store'
import type { IProvider, QueryResult, Query } from '../Provider'
import { ProviderType } from '../Provider'
import { query as serviceQuery } from '../Providers/Service'
import { query as storageQuery } from '../Providers/Storage'
import { query as inheritedQuery } from '../Providers/Inherited'
import type { DataRequestAction } from '../Actions'
import type { DataSourceState } from '../DataSource'

function getQuery<
    TProvider extends IProvider,
    TProviderType extends ProviderType = TProvider['type']
>(provider: TProviderType): Query<TProvider> {
    switch (provider) {
        case undefined:
        case ProviderType.service: { return serviceQuery as Query<IProvider> }
        case ProviderType.storage: { return storageQuery as Query<IProvider> }
        case ProviderType.inherited: { return inheritedQuery as Query<IProvider> }
        default: { return () => { throw new Error(`hasn't implementation for provider type: "${provider}`) } }
    }
}

export function* dataRequest({ payload }: DataRequestAction) {
    const { id, options = {} } = payload

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

        yield put(setModel(ModelPrefix.source, id, response.list))
        yield put(resolveRequest(id, response))
    } catch (error) {
        const err = error as { message: string, stack: string, json?: { meta: IMeta} }
        const errorMeta = err?.json?.meta || {}

        if (errorMeta.messages) {
            const fields: Record<string, IValidationFieldMessage[]> = {}

            for (const [fieldName, error] of Object.entries(errorMeta.messages.fields)) {
                fields[fieldName] = Array.isArray(error) ? error : [error]
            }

            yield put(failValidate(id, fields, ModelPrefix.filter, { touched: true }))
        }

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
