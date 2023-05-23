import { put, select, takeEvery } from 'redux-saga/effects'
import { createAction } from '@reduxjs/toolkit'
import { push } from 'connected-react-router'
import get from 'lodash/get'
import isUndefined from 'lodash/isUndefined'

import { Action, Meta } from '../Action'
import { State as GlobalState } from '../State'
// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../../core/dataProviderResolver'
import evalExpression, { parseExpression } from '../../utils/evalExpression'

import { EffectWrapper } from './utils/effectWrapper'
import { PAGE_PREFIX, INVALID_URL_MESSAGE } from './constants'

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

export function* openPageEffect({ payload }: Action<string, OpenPagePayload>) {
    const state: GlobalState = yield select()

    const { url, pathMapping, queryMapping, target, modelLink, restore = false } = payload

    let compiledUrl = null

    if (modelLink) {
        const model = get(state, modelLink)
        const parsedExpressionUrl = parseExpression(url)

        compiledUrl = parsedExpressionUrl ? evalExpression(parsedExpressionUrl, model) : url

        if (isUndefined(compiledUrl)) {
            throw new Error(INVALID_URL_MESSAGE)
        }
    } else {
        const { url: dataProviderUrl } = dataProviderResolver(state, {
            url,
            pathMapping,
            queryMapping,
        })

        compiledUrl = dataProviderUrl
    }

    if (restore) {
        const { global = {} } = state
        const query: string = get(global, `breadcrumbs.${compiledUrl}`, '')

        compiledUrl = `${compiledUrl}${query}`
    }

    if (target === 'application') {
        yield put(push(compiledUrl))
    } else if (target === '_blank') {
        window.open(compiledUrl)
    } else {
        window.location = compiledUrl
    }
}

export const sagas = [
    takeEvery(openPagecreator.type, EffectWrapper(openPageEffect)),
]
