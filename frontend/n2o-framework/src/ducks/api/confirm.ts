import { createAction } from '@reduxjs/toolkit'
import { takeEvery, put, select } from 'redux-saga/effects'
import get from 'lodash/get'
import isObject from 'lodash/isObject'
import isEmpty from 'lodash/isEmpty'

import { insert } from '../overlays/store'
import { Meta } from '../Action'
import evalExpression, { parseExpression } from '../../utils/evalExpression'
// @ts-ignore ignore import error from js file
import linkResolver from '../../utils/linkResolver'

import { ACTIONS_PREFIX } from './constants'
import { startOperation } from './Operation'

export type Payload = {
    name: string
    visible: boolean
    mode: string
    type: string
    modelLink?: string
    condition?: string | boolean
    text?: string
}

export const creator = createAction(
    `${ACTIONS_PREFIX}confirm`,
    (payload: Payload, meta: Meta) => ({
        payload,
        meta,
    }),
)

const resolveConditions = (model: Record<string, unknown>, condition?: string | boolean) => {
    if (!condition) {
        return true
    }
    if (isObject(condition) && isEmpty(condition)) {
        return true
    }
    if (typeof condition === 'boolean') {
        return condition
    }

    const expression = parseExpression(condition)

    if (expression) {
        return evalExpression(expression, model)
    }

    return false
}

function* resolve(props: Pick<Payload, 'modelLink' | 'condition' | 'text'>) {
    const { modelLink } = props

    if (!modelLink) {
        return props
    }

    const state = yield select()
    const model = get(state, modelLink)
    const { condition } = props

    if (!resolveConditions(model, condition)) {
        return null
    }

    const { text } = props
    const resolvedText = linkResolver(state, { link: modelLink, value: text })

    return { ...props, text: resolvedText }
}

/* TODO OverlaysRefactoring убрать передаваемый type */
export function* effect({ payload, meta, type: actionType }: ReturnType<typeof creator>) {
    const { name, visible = true, mode, type = 'confirm', ...props } = payload
    const { operationId = null, target } = meta

    if (operationId) {
        yield put(startOperation(actionType, operationId))
    }

    const resolvedProps = { ...yield resolve(props), target, operation: { id: operationId, type: actionType } }

    yield put(insert(name, visible, mode, type, resolvedProps))
}

export const sagas = [takeEvery(creator.type, effect)]
