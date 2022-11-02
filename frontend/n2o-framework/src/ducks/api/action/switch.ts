import { createAction, Action } from '@reduxjs/toolkit'
import { get, isEmpty } from 'lodash'
import { put, select } from 'redux-saga/effects'

import { ModelPrefix } from '../../../core/datasource/const'
import { makeGetModelByPrefixSelector } from '../../models/selectors'
import { ACTIONS_PREFIX } from '../constants'

export type Payload = {
    datasource: string
    model: ModelPrefix
    valueFieldId: string
    defaultAction: Action
    cases: Record<string, Action>
}

export const creator = createAction(
    `${ACTIONS_PREFIX}switch`,
    (payload: Payload, meta: object) => ({
        payload,
        meta,
    }),
)

export function* effect({ payload }: ReturnType<typeof creator>) {
    const { datasource, model: modelPrefix, defaultAction, cases, valueFieldId } = payload
    const model: object = yield select(makeGetModelByPrefixSelector(modelPrefix, datasource))
    const action = cases[get(model, valueFieldId)] || defaultAction

    if (!isEmpty(action)) {
        yield put(action)
    }
}
