import { call, takeEvery } from 'redux-saga/effects'
import { eventChannel, EventChannel } from 'redux-saga'

import { local } from '../utils/Storage'

export function createStorageChannel<D extends object>(key: string) {
    return eventChannel<D>((emit) => {
        return local.subscribe((changedKey, value: object | null) => {
            if (changedKey === key) {
                emit((value as D) || {} as D)
            }
        })
    })
}

export function* watchLocalStorage<D extends object, O>(key: string, callback: (data: D, options: O) => void, options: O) {
    const channel: EventChannel<D> = yield call(createStorageChannel<D>, key)

    yield takeEvery(channel, function* executor(data: D) {
        yield call(callback, data, options)
    })
}
