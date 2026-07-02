import { createAction } from '@reduxjs/toolkit'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import { select } from 'redux-saga/effects'

import { Action, ErrorAction, Meta } from '../../Action'
import { ModelPrefix } from '../../../core/datasource/const'
import { getByLinkSelector } from '../../models/selectors'
import { ACTIONS_PREFIX } from '../constants'
import { waitOperation } from '../utils/waitOperation'
import { mergeMeta } from '../utils/mergeMeta'

export type Payload = {
    datasource: string
    model: ModelPrefix
    field?: string
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

export function* effect({ payload, meta }: ReturnType<typeof creator>) {
    const { datasource: id, model: prefix, field, defaultAction, cases, valueFieldId } = payload
    const model: Record<string, unknown> = yield select(getByLinkSelector({ id, prefix, field }))
    const action = cases[get(model, valueFieldId) as keyof typeof cases] || defaultAction

    if (!isEmpty(action)) {
        const resultAction: Action | ErrorAction = yield waitOperation(mergeMeta(action, meta))

        if (resultAction.error) {
            throw new Error(resultAction.error)
        }
    }
}
