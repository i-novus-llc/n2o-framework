import { put, select, takeEvery } from 'redux-saga/effects'
import { createAction } from '@reduxjs/toolkit'
import { push } from 'connected-react-router'
import get from 'lodash/get'
import isUndefined from 'lodash/isUndefined'

import { Action, Meta } from '../Action'
import { State as GlobalState } from '../State'
// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { executeExpression } from '../../core/Expression/execute'
import { parseExpression } from '../../core/Expression/parse'

import { EffectWrapper } from './utils/effectWrapper'
import { PAGE_PREFIX, INVALID_URL_MESSAGE } from './constants'
import { stopTheSequence } from './utils/stopTheSequence'

export type OpenPagePayload = {
    url: string
    modelLink?: string
    pathMapping: object
    queryMapping: object
    target?: '_blank' | 'application'
    restore?: boolean
}

export const openPagecreator = createAction(
    `${PAGE_PREFIX}open`,
    (payload: OpenPagePayload, meta: Meta) => ({
        payload,
        meta,
    }),
)

export function* openPageEffect(action: Action<string, OpenPagePayload>) {
    const { payload, meta = {} } = action
    const state: GlobalState = yield select()

    const { url, pathMapping, queryMapping, target, modelLink, restore = false } = payload
    const { evalContext } = meta

    let compiledUrl = null

    if (modelLink) {
        const model = get(state, modelLink)
        const parsedExpressionUrl = parseExpression(url)

        compiledUrl = parsedExpressionUrl ? executeExpression(parsedExpressionUrl, model, evalContext) : url

        if (isUndefined(compiledUrl)) {
            throw new Error(INVALID_URL_MESSAGE)
        }
    } else {
        // @ts-ignore import from js file
        const { url: dataProviderUrl } = dataProviderResolver(state, { url, pathMapping, queryMapping })

        compiledUrl = dataProviderUrl
    }

    if (restore) {
        const { global = {} } = state
        const query: string = get(global, `breadcrumbs.${compiledUrl}`, '')

        compiledUrl = `${compiledUrl}${query}`
    }

    if (target === 'application') {
        // @ts-ignore import from js file
        yield put(push(compiledUrl))
    } else if (target === '_blank') {
        // @ts-ignore import from js file
        window.open(compiledUrl)
    } else {
        // @ts-ignore import from js file
        window.location = compiledUrl
    }

    stopTheSequence(action)
}

export const sagas = [
    takeEvery(openPagecreator.type, EffectWrapper(openPageEffect)),
]
