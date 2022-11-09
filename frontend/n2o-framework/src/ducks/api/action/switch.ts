import { createAction } from '@reduxjs/toolkit'
import { get, isEmpty } from 'lodash'
import { put, select } from 'redux-saga/effects'

import { Action, ErrorAction, Meta } from '../../Action'
import { ModelPrefix } from '../../../core/datasource/const'
import { makeGetModelByPrefixSelector } from '../../models/selectors'
import { ACTIONS_PREFIX } from '../constants'
import { mergeMeta } from '../utils/mergeMeta'
import { waitOperation } from '../utils/waitOperation'
import { guid } from '../../../utils/id'

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
        const operationId = guid()

        yield put(mergeMeta(action, {
            operationId,
        }))

        const resultAction: Action | ErrorAction = yield waitOperation(operationId)

        if (!resultAction.error) {
            throw new Error(resultAction.error)
        }
    }
}
