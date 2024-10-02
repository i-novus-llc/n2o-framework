import { createAction } from '@reduxjs/toolkit'
import { cancel, put } from 'redux-saga/effects'

import { Action, ErrorAction, Meta } from '../../Action'
import { ACTIONS_PREFIX } from '../constants'
import { waitOperation } from '../utils/waitOperation'
import { mergeMeta } from '../utils/mergeMeta'
import { failOperation } from '../Operation'

export type Payload = { actions: Action[] }
export interface SequenceMeta extends Meta { key: string, buttonId: string }

export const creator = createAction(
    `${ACTIONS_PREFIX}sequence`,
    (payload: Payload, meta: SequenceMeta) => ({ payload, meta }),
)

export const finisher = createAction(
    `${ACTIONS_PREFIX}sequence_end`,
    (payload: Record<string, unknown>, meta: SequenceMeta) => ({ payload, meta }),
)

export function* effect({ payload, meta }: ReturnType<typeof creator>) {
    const { actions } = payload
    const { target, key, buttonId, evalContext } = meta

    for (const action of actions) {
        const resultAction: Action | ErrorAction = yield waitOperation(
            mergeMeta(action, { target, key, buttonId, evalContext }),
        )

        if (resultAction.error) { throw new Error(resultAction.error) }
        if (resultAction.type === failOperation.type) { yield cancel() }
    }

    yield put(finisher({}, { target, key, buttonId, evalContext }))
}
