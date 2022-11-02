import { createAction } from '@reduxjs/toolkit'
import { Task } from 'redux-saga'
import { call, fork, put } from 'redux-saga/effects'

import { ACTIONS_PREFIX } from '../constants'

import { Action } from './util'

const create = (type: string) => createAction(`${ACTIONS_PREFIX}${type}`, (payload: string, meta: object) => ({ payload, meta }))

export const startEffect = create('startEffect')
export const successEffect = create('successEffect')
export const failEffect = create('failEffect')

/* eslint-disable indent */
/*
 * eslint тут чего-то сходит с ума на отступах
 * FIXME разобраться
 */
export const wrapEffect = <
    TAction extends Action
>(
    effect,
    effectName: string,
) => function* effectWrapper(...args: [...unknown[], TAction]) {
    const [...rest, action] = args
    const { meta } = action
    const { sequence_id } = meta

    if (!sequence_id) {
        yield call(effect, action)

        return
    }

    yield put(startEffect(effectName, { sequence_id }))

    const task: Task = yield fork(effect, ...rest, action)

    const endAction: Action = yield task.toPromise().then(
        () => successEffect(effectName, { sequence_id }),
        (error: unknown) => failEffect(effectName, { sequence_id, error }),
    )

    yield put(endAction)
}
/* eslint-enable indent */
