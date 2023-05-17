import { Action } from 'redux'
import { take, race, delay, put } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import {
    failOperation,
    OperationAction,
    startOperation,
    successOperation,
} from '../Operation'
import { Action as N2OAction } from '../../Action'
import { guid } from '../../../utils/id'

import { mergeMeta } from './mergeMeta'

const WAIT_START_ACTION_TIMEOUT = 500

const getPredicate = (type: string, uid: string) => (action: Action) => {
    if (action.type !== type) {
        return false
    }

    const { payload } = action as OperationAction

    return payload?.uid === uid
}

/**
 * Хелпер для ожидания выполнения сабэфектов для экшена
 */
export function* waitOperation(action: N2OAction) {
    const operationId = guid()

    yield put(mergeMeta(action, {
        operationId,
    }))

    const { init } = yield race({
        init: take(getPredicate(startOperation.type, operationId)),
        timeout: delay(WAIT_START_ACTION_TIMEOUT),
    })

    if (isEmpty(init)) { return successOperation('waiting start operation timeout', operationId) }

    const { success, fail } = yield race({
        success: take(getPredicate(successOperation.type, operationId)),
        fail: take(getPredicate(failOperation.type, operationId)),
    })

    return success || fail
}
