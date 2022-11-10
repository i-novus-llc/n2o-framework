import { createAction } from '@reduxjs/toolkit'
import { isEmpty } from 'lodash'
import { put, select } from 'redux-saga/effects'

import { Action, ErrorAction, Meta } from '../../Action'
import { ModelPrefix } from '../../../core/datasource/const'
import evalExpression from '../../../utils/evalExpression'
import { makeGetModelByPrefixSelector } from '../../models/selectors'
import { ACTIONS_PREFIX } from '../constants'
import { mergeMeta } from '../utils/mergeMeta'
import { waitOperation } from '../utils/waitOperation'
import { guid } from '../../../utils/id'

export type Payload = {
    datasource: string
    model: ModelPrefix
    condition: string,
    success: Action,
    fail: Action
}

export const creator = createAction(
    `${ACTIONS_PREFIX}condition`,
    (payload: Payload, meta: Meta) => ({
        payload,
        meta,
    }),
)

export function* effect({ payload }: Action<string, Payload>) {
    const { datasource, model: modelPrefix, condition, success, fail } = payload
    const model: object = yield select(makeGetModelByPrefixSelector(modelPrefix, datasource))
    const action = evalExpression(condition, model) ? success : fail

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
