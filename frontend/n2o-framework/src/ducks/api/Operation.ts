import { createAction } from '@reduxjs/toolkit'

import { Action } from '../Action'

import { ACTIONS_PREFIX } from './constants'

type Payload = {
    name: string
    uid: string
}

export type OparationAction = Action<string, Payload>

type Creator = ((name: string, uid: string) => OparationAction) & { type: OparationAction['type'] }

const create = (type: string): Creator => createAction(
    `${ACTIONS_PREFIX}${type}`,
    (name: string, uid: string) => ({ payload: { name, uid }, meta: {} }),
)

export const startOperation = create('start_operation')
export const successOperation = create('success_operation')
export const failOperation = createAction(
    `${ACTIONS_PREFIX}fail_operation`,
    (name: string, uid: string, error) => ({ payload: { name, uid }, error, meta: {} }),
)
