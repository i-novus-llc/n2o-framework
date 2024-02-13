import { call, put, take, cancelled } from 'redux-saga/effects'

import { fetchStart, fetchEnd, fetchCancel, fetchError } from '../actions/fetch'
import { defaultApiProviderEnhanced } from '../core/api'
import { FETCH_ERROR_CONTINUE } from '../constants/fetch'

export default function* fetchSaga(
    fetchType: string,
    options: Record<string, unknown>,
    apiProvider = defaultApiProviderEnhanced,
): unknown {
    const abortController = new AbortController()

    try {
        yield put(fetchStart(fetchType, options))
        // @ts-ignore import from js file
        const response: Record<string, unknown> = yield call(apiProvider, fetchType, options, abortController.signal)

        yield put(fetchEnd(fetchType, options, response))

        return response
    } catch (error) {
        yield put(fetchError(fetchType, options, error))
        yield take(FETCH_ERROR_CONTINUE)
        throw error
    } finally {
        if (yield cancelled()) {
            abortController.abort()
            yield put(fetchCancel(fetchType, options))
        }
    }
}
