import { createAction, Action } from '@reduxjs/toolkit'
import { get, isEmpty } from 'lodash'
import { put, select, takeEvery } from 'redux-saga/effects'

import { ModelPrefix } from '../../core/datasource/const'
import evalExpression from '../../utils/evalExpression'
import { makeGetModelByPrefixSelector } from '../models/selectors'

import { API_PREFIX } from './constants'

const ACTIONS_PREFIX = `${API_PREFIX}action/`

export type ConditionPayload = {
    datasource: string
    model: ModelPrefix
    condition: string,
    success: Action,
    fail: Action
}
export const conditionCreator = createAction(
    `${ACTIONS_PREFIX}condition`,
    (payload: ConditionPayload, meta: object) => ({
        payload,
        meta,
    }),
)
function* ConditionEffect({ payload }: ReturnType<typeof conditionCreator>) {
    const { datasource, model: modelPrefix, condition, success, fail } = payload
    const model: object = yield select(makeGetModelByPrefixSelector(modelPrefix, datasource))
    const action = evalExpression(condition, model) ? success : fail

    if (!isEmpty(action)) {
        yield put(action)
    }
}

export type SwitchPayload = {
    datasource: string
    model: ModelPrefix
    valueFieldId: string
    defaultAction: Action
    cases: Record<string, Action>
}
export const switchCreator = createAction(
    `${ACTIONS_PREFIX}switch`,
    (payload: SwitchPayload, meta: object) => ({
        payload,
        meta,
    }),
)
function* SwitchEffect({ payload }: ReturnType<typeof switchCreator>) {
    const { datasource, model: modelPrefix, defaultAction, cases, valueFieldId } = payload
    const model: object = yield select(makeGetModelByPrefixSelector(modelPrefix, datasource))
    const action = cases[get(model, valueFieldId)] || defaultAction

    if (!isEmpty(action)) {
        yield put(action)
    }
}

export const sagas = [
    takeEvery(conditionCreator.type, ConditionEffect),
    takeEvery(switchCreator.type, SwitchEffect),
]
