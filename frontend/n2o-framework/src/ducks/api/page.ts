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
import { makePageByIdSelector } from '../pages/selectors'
import { resolvePath } from '../../components/core/router/resolvePath'
import { setLocation } from '../pages/store'

import { EffectWrapper } from './utils/effectWrapper'
import { PAGE_PREFIX, INVALID_URL_MESSAGE } from './constants'
import { stopTheSequence } from './utils/stopTheSequence'

export type OpenPagePayload = {
    url: string
    modelLink?: string
    pathMapping: object
    queryMapping: object
    target?: '_blank' | 'application' | '_self'
    restore?: boolean
}

export const openPageCreator = createAction(
    `${PAGE_PREFIX}open`,
    (payload: OpenPagePayload, meta: Meta) => ({
        payload,
        meta,
    }),
)

function getAnchorPage(url: string, state: GlobalState, pageId?: string): string | null {
    if (!pageId) { return null }

    const page = makePageByIdSelector(pageId)(state)

    if (!page || page.rootPage) { return null }
    if (page.parentId) { return getAnchorPage(url, state, page.parentId) }

    const isAnchor = url.startsWith(page.pageUrl) &&
        page.metadata?.routes?.subRoutes?.some(route => url.startsWith(
            resolvePath(page.pageUrl, route),
        ))

    return isAnchor ? pageId : null
}

function* openPage(
    url: string,
    target: OpenPagePayload['target'],
    pageId?: string,
) {
    if (target === '_blank') {
        window.open(url)

        return
    }
    if (target === '_self') {
        window.location = url as string & Location

        return
    }

    const state: GlobalState = yield select()
    const anchorPageId = getAnchorPage(url, state, pageId)

    if (anchorPageId) {
        yield put(setLocation(pageId, url))
    } else {
        yield put(push(url))
    }
}

export function* openPageEffect(action: Action<string, OpenPagePayload>) {
    const { payload, meta = {} } = action
    const state: GlobalState = yield select()

    const { url, pathMapping, queryMapping, target, modelLink, restore = false } = payload
    const { evalContext, pageId } = meta

    let compiledUrl = ''

    if (modelLink) {
        const model = get(state, modelLink)
        const parsedExpressionUrl = parseExpression(url)

        compiledUrl = parsedExpressionUrl ? executeExpression(parsedExpressionUrl, model, evalContext) : url

        if (isUndefined(compiledUrl)) {
            throw new Error(INVALID_URL_MESSAGE)
        }
    } else {
        const { url: dataProviderUrl } = dataProviderResolver(state, { url, pathMapping, queryMapping })

        compiledUrl = dataProviderUrl
    }

    if (restore) {
        const { global = {} } = state
        const query: string = get(global, `breadcrumbs.${compiledUrl}`, '')

        compiledUrl = `${compiledUrl}${query}`
    }

    if (typeof compiledUrl !== 'string') {
        throw new Error('Compiled URL must be a string')
    }

    const encodedUrl = encodeURI(compiledUrl)

    yield openPage(encodedUrl, target, pageId)

    stopTheSequence(action)
}

export const sagas = [
    takeEvery(openPageCreator.type, EffectWrapper(openPageEffect)),
]
