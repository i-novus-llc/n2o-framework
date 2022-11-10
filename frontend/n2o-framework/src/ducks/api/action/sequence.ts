import { createAction } from '@reduxjs/toolkit'
import { put } from 'redux-saga/effects'

import { Action, ErrorAction, Meta } from '../../Action'
import { guid } from '../../../utils/id'
import { ACTIONS_PREFIX } from '../constants'
import { mergeMeta } from '../utils/mergeMeta'
import { waitOperation } from '../utils/waitOperation'

export type Payload = {
    actions: Action[]
}

export const creator = createAction(
    `${ACTIONS_PREFIX}sequence`,
    (payload: Payload, meta: Meta) => ({
        payload,
        meta,
    }),
)

export function* effect({ payload }: ReturnType<typeof creator>) {
    const { actions } = payload

    for (const action of actions) {
        const operationId = guid()

        yield put(mergeMeta(action, {
            operationId,
        }))

        const resultAction: Action | ErrorAction = yield waitOperation(operationId)

        if (!resultAction.error) {
            throw new Error(resultAction.error)
        }
    }
}
