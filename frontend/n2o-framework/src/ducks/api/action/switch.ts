import { createAction } from '@reduxjs/toolkit'
import { get, isEmpty } from 'lodash'
import { select } from 'redux-saga/effects'

import { Action, ErrorAction, Meta } from '../../Action'
import { ModelPrefix } from '../../../core/datasource/const'
import { getModelByPrefixAndNameSelector } from '../../models/selectors'
import { ACTIONS_PREFIX } from '../constants'
import { waitOperation } from '../utils/waitOperation'
import { mergeMeta } from '../utils/mergeMeta'

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

export function* effect({ payload, meta }: ReturnType<typeof creator>) {
    const { datasource, model: modelPrefix, defaultAction, cases, valueFieldId } = payload
    const { target, evalContext } = meta
    const model: object = yield select(getModelByPrefixAndNameSelector(modelPrefix, datasource))
    const action = cases[get(model, valueFieldId)] || defaultAction

    if (!isEmpty(action)) {
        const resultAction: Action | ErrorAction = yield waitOperation(mergeMeta(action, { target, evalContext }))

        if (resultAction.error) {
            throw new Error(resultAction.error)
        }
    }
}
