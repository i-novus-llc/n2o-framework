import { createAction } from '@reduxjs/toolkit'
import { put, take } from 'redux-saga/effects'

import { guid } from '../../../utils/id'
import { ACTIONS_PREFIX } from '../constants'

import { Action, mergeMeta } from './util'

export type Payload = {
    actions: Action[]
}

export const creator = createAction(
    `${ACTIONS_PREFIX}sequence`,
    (payload: Payload, meta: object) => ({
        payload,
        meta,
    }),
)

export function* effect({ payload }: ReturnType<typeof creator>) {
    const { actions } = payload
    const id = guid()

    for (const action of actions) {
        const sequenceAction = mergeMeta(action, {
            sequence_id: id,
        })

        yield put(sequenceAction)
        yield take()
    }
}
