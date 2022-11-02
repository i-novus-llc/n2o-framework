import { createAction, Action } from '@reduxjs/toolkit'
import { isEmpty } from 'lodash'
import { put, select } from 'redux-saga/effects'

import { ModelPrefix } from '../../../core/datasource/const'
import evalExpression from '../../../utils/evalExpression'
import { makeGetModelByPrefixSelector } from '../../models/selectors'
import { ACTIONS_PREFIX } from '../constants'

export type Payload = {
    datasource: string
    model: ModelPrefix
    condition: string,
    success: Action,
    fail: Action
}

export const creator = createAction(
    `${ACTIONS_PREFIX}condition`,
    (payload: Payload, meta: object) => ({
        payload,
        meta,
    }),
)

export function* effect({ payload }: ReturnType<typeof creator>) {
    const { datasource, model: modelPrefix, condition, success, fail } = payload
    const model: object = yield select(makeGetModelByPrefixSelector(modelPrefix, datasource))
    const action = evalExpression(condition, model) ? success : fail

    if (!isEmpty(action)) {
        yield put(action)
    }
}
