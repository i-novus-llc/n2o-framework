import { call, cancelled } from 'redux-saga/effects'

import { defaultApiProviderEnhanced } from '../core/api'

export default function* fetchSaga(
    fetchType: string,
    options: Record<string, unknown>,
    apiProvider = defaultApiProviderEnhanced,
): unknown {
    const abortController = new AbortController()

    try {
        // @ts-ignore import from js file
        const response: Record<string, unknown> = yield call(apiProvider, fetchType, options, abortController.signal)

        return response
    } finally {
        if (yield cancelled()) {
            abortController.abort()
        }
    }
}
