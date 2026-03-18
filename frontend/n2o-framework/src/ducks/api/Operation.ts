import { createAction } from '@reduxjs/toolkit'

import { Action } from '../Action'

import { ACTIONS_PREFIX } from './constants'

type Payload = {
    name: string
    operationId: string
    result?: unknown
}

export type OperationAction = Action<string, Payload>

export const startOperation = createAction(
    `${ACTIONS_PREFIX}start_operation`,
    (name: string, operationId: string, meta) => ({ payload: { name, operationId }, meta: meta || {} }),
)
export const successOperation = createAction(
    `${ACTIONS_PREFIX}success_operation`,
    (name: string, operationId: string, result?: unknown, meta?) => ({
        payload: { name, operationId, result },
        meta: meta || {},
    }),
)
export const failOperation = createAction(
    `${ACTIONS_PREFIX}fail_operation`,
    (name: string, operationId: string, error, meta?) => ({
        payload: { name, operationId },
        error,
        meta: meta || {},
    }),
)
