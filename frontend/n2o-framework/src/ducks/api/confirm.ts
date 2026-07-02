import { createAction } from '@reduxjs/toolkit'
import { takeEvery, put, select } from 'redux-saga/effects'
import isObject from 'lodash/isObject'
import isEmpty from 'lodash/isEmpty'

import { insert } from '../overlays/store'
import { Meta } from '../Action'
import { executeExpression } from '../../core/Expression/execute'
import { parseExpression } from '../../core/Expression/parse'
import { propsResolver } from '../../core/Expression/propsResolver'
import { ModelPrefix } from '../../core/models/types'
import { getByLinkSelector } from '../models/selectors'

import { ACTIONS_PREFIX } from './constants'
import { startOperation } from './Operation'

export type Payload = {
    name: string
    visible: boolean
    mode: string
    type: string
    datasource: string
    model: ModelPrefix
    field?: string
    condition?: string | boolean
    text?: string
}

export const creator = createAction(
    `${ACTIONS_PREFIX}confirm`,
    (payload: Payload, meta: Meta) => ({ payload, meta }),
)

const resolveConditions = (
    model: Record<string, unknown>,
    condition?: string | boolean,
    ctx?: Record<string, unknown>,
) => {
    if (!condition) { return true }
    if (isObject(condition) && isEmpty(condition)) { return true }
    if (typeof condition === 'boolean') { return condition }

    const expression = parseExpression(condition)

    if (expression) { return executeExpression(expression, model, ctx) }

    return false
}

function* resolve(
    props: Omit<Payload, 'name' | 'mode' | 'visible' | 'type'>,
    ctx?: Record<string, unknown>,
) {
    const { datasource: id, model: prefix, field, condition, ...rest } = props
    const model: Record<string, unknown> = yield select(getByLinkSelector({ id, prefix, field }))

    if (!resolveConditions(model, condition, ctx)) { return {} }

    return propsResolver(rest, model, ctx)
}

/* TODO OverlaysRefactoring убрать передаваемый type */
export function* effect({ payload, meta, type: actionType }: ReturnType<typeof creator>) {
    const { name, mode, visible = true, type = 'confirm', ...props } = payload
    const { target, buttonId, key, operationId = null, evalContext } = meta

    if (operationId) {
        yield put(startOperation(actionType, operationId, { key, buttonId }))
    }

    const resolved: Record<string, unknown> = yield resolve(props, evalContext)

    const resolvedProps = {
        ...resolved, target, operation: { id: operationId, type: actionType, buttonId, key },
    }

    yield put(insert(name, visible, mode, type, resolvedProps))
}

export const sagas = [takeEvery(creator.type, effect)]
