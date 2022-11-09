import { Action } from 'redux'
import { take, race, delay } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { failOperation, OparationAction, startOperation, successOperation } from '../Operation'

const WAIT_START_ACTION_TIMEOUT = 500

const getPredicate = (type: string, uid: string) => (action: Action) => {
    if (action.type !== type) {
        return false
    }

    const { payload } = action as OparationAction

    return payload?.uid === uid
}

/**
 * Хелпер для ожидания выполнения сабэфектов с указанным operationId
 * @param {string} uid
 */
export function* waitOperation(uid: string) {
    const { action } = yield race({
        action: take(getPredicate(startOperation.type, uid)),
        timeout: delay(WAIT_START_ACTION_TIMEOUT),
    })

    if (!isEmpty(action)) { return successOperation('waiting start operation timeout', uid) }

    const { success, fail } = yield race({
        success: take(getPredicate(successOperation.type, uid)),
        fail: take(getPredicate(failOperation.type, uid)),
    })

    return success || fail
}
