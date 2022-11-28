import { createAction } from '@reduxjs/toolkit'
import { get, isEmpty } from 'lodash'
import { select } from 'redux-saga/effects'

import { Action, ErrorAction, Meta } from '../../Action'
import { ModelPrefix } from '../../../core/datasource/const'
import { makeGetModelByPrefixSelector } from '../../models/selectors'
import { ACTIONS_PREFIX } from '../constants'
import { waitOperation } from '../utils/waitOperation'

export type Payload = {
    datasource: string
    model: ModelPrefix
    valueFieldId: string
    defaultAction: Action
    cases: Record<string, Action>
}

export const creator = createAction(
    `${ACTIONS_PREFIX}switch`,
    (payload: Payload, meta: Meta) => ({
        payload,
        meta,
    }),
)

export function* effect({ payload }: ReturnType<typeof creator>) {
    const { datasource, model: modelPrefix, defaultAction, cases, valueFieldId } = payload
    const model: object = yield select(makeGetModelByPrefixSelector(modelPrefix, datasource))
    const action = cases[get(model, valueFieldId)] || defaultAction

    if (!isEmpty(action)) {
        const resultAction: Action | ErrorAction = yield waitOperation(action)

        if (resultAction.error) {
            throw new Error(resultAction.error)
        }
    }
}
