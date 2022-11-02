import { createAction } from '@reduxjs/toolkit'
import { take } from 'lodash'
import { Task } from 'redux-saga'
import { call, fork, put } from 'redux-saga/effects'

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

export const simple = <TAction extends Action>(effect) => function* effectWrapper(action: TAction) {
    const { meta } = action

    if (!meta.sequence_id) {
        yield call(effect, action)

        return
    }

    const task: Task = yield fork(effect, action)

    const p = task.toPromise().then(() => {
        console.log(123)
    })

    console.log(p)
}

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
