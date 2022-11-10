import { createAction } from '@reduxjs/toolkit'

import { Action, ErrorAction, Meta } from '../../Action'
import { ACTIONS_PREFIX } from '../constants'
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
        const resultAction: Action | ErrorAction = yield waitOperation(action)

        if (resultAction.error) {
            throw new Error(resultAction.error)
        }
    }
}
