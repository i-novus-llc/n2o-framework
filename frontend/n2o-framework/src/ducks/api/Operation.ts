import { createAction } from '@reduxjs/toolkit'

import { Action } from '../Action'

import { ACTIONS_PREFIX } from './constants'

type Payload = {
    name: string
    uid: string
    result?: unknown
}

export type OperationAction = Action<string, Payload>

export const startOperation = createAction(
    `${ACTIONS_PREFIX}start_operation`,
    (name: string, uid: string, meta) => ({ payload: { name, uid }, meta: meta || {} }),
)
export const successOperation = createAction(
    `${ACTIONS_PREFIX}success_operation`,
    (name: string, uid: string, result?: unknown, meta?) => ({ payload: { name, uid, result }, meta: meta || {} }),
)
export const failOperation = createAction(
    `${ACTIONS_PREFIX}fail_operation`,
    (name: string, uid: string, error, meta?) => ({ payload: { name, uid }, error, meta: meta || {} }),
)
