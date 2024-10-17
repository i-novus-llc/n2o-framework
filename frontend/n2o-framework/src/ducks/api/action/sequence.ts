import { createAction } from '@reduxjs/toolkit'
import { cancel, put } from 'redux-saga/effects'
import get from 'lodash/get'

import { Action, ErrorAction, Meta } from '../../Action'
import { ACTIONS_PREFIX } from '../constants'
import { waitOperation } from '../utils/waitOperation'
import { mergeMeta } from '../utils/mergeMeta'
import { failOperation } from '../Operation'

export type Payload = { actions: Action[], fallback: Action }
export interface SequenceMeta extends Meta { key: string, buttonId: string }

export const creator = createAction(
    `${ACTIONS_PREFIX}sequence`,
    (payload: Payload, meta: SequenceMeta) => ({ payload, meta }),
)

export const finisher = createAction(
    `${ACTIONS_PREFIX}sequence_end`,
    (payload: Record<string, unknown>, meta: SequenceMeta) => ({ payload, meta }),
)

let abortController = new AbortController()

export function* effect({ payload, meta }: ReturnType<typeof creator>) {
    const { actions, fallback } = payload
    const { target, key, buttonId, evalContext } = meta

    for (const action of actions) {
        const resultAction: Action | ErrorAction = yield waitOperation(
            mergeMeta(action, { target, key, buttonId, evalContext, abortController }),
        )

        if (resultAction.error) {
            const { meta } = resultAction
            const aborted = get(meta, 'abortController.signal.aborted', false)

            if (aborted) {
                abortController = new AbortController()
                throw new Error(resultAction.error)
            }

            if (fallback) {
                yield put({ ...fallback })
            }

            throw new Error(resultAction.error)
        }
        if (resultAction.type === failOperation.type) {
            yield cancel()
        }
    }

    yield put(finisher({}, { target, key, buttonId, evalContext }))
}
