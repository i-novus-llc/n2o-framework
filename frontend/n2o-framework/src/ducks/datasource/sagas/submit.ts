import { select, put } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { dataSourceByIdSelector } from '../selectors'
import type { SubmitProvider } from '../Provider'
import { ProviderType } from '../Provider'
import { submit as submitService } from '../Providers/Service'
import { submit as submitStorage } from '../Providers/Storage'
import { submit as submitInherited } from '../Providers/Inherited'
import { submit as submitCached } from '../Providers/Cached'
import type { SubmitAction } from '../Actions'
import type { DataSourceState } from '../DataSource'
import { submitFail, submitSuccess } from '../store'

type SubmitMethod<TProvider extends SubmitProvider = SubmitProvider> = (
    id: string,
    provider: TProvider,
    apiProvider: unknown,
) => Generator

function getSubmit<
    TSubmit extends SubmitProvider,
    TSubmitType extends ProviderType = TSubmit['type'],
>(provider: TSubmitType): SubmitMethod<TSubmit> {
    switch (provider) {
        case undefined:
        case ProviderType.service: { return submitService as SubmitMethod<TSubmit> }
        case ProviderType.storage: { return submitStorage as SubmitMethod<TSubmit> }
        case ProviderType.inherited: { return submitInherited as SubmitMethod<TSubmit> }
        case ProviderType.cached: { return submitCached as SubmitMethod<TSubmit> }
        default: { return () => { throw new Error(`hasn't implementation for provider type: "${provider}`) } }
    }
}

export function* submitSaga(apiProvider: unknown, { meta, payload }: SubmitAction) {
    const { id, provider } = payload

    try {
        const { submit }: DataSourceState = yield select(dataSourceByIdSelector(id))

        const submitProvider = provider || submit

        if (!submitProvider || isEmpty(submitProvider)) {
            throw new Error('Can\'t submit data with empty provider')
        }

        // TODO validate model here

        const submitMethod = getSubmit(submitProvider.type)

        yield submitMethod(id, submitProvider, apiProvider)
        yield put(submitSuccess(meta?.success))
    } catch (error) {
        // @ts-ignore поправить типы
        yield put(submitFail(error, meta?.fail))
        // eslint-disable-next-line no-console
        console.warn(`JS Error: DataSource(${id}) submit saga. ${error instanceof Error ? error.message : error}`)
    }
}
