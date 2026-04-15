import { put, select, takeEvery, call } from 'redux-saga/effects'
import { createAction } from '@reduxjs/toolkit'
import { push } from 'connected-react-router'
import get from 'lodash/get'
import isUndefined from 'lodash/isUndefined'
import i18next from 'i18next'

import { type Action, type Meta } from '../Action'
import { type State as GlobalState } from '../State'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { executeExpression } from '../../core/Expression/execute'
import { parseExpression } from '../../core/Expression/parse'
import { setLocation, showPagePrompt } from '../pages/store'

import { AsyncEffectWrapper } from './utils/effectWrapper'
import { PAGE_PREFIX, INVALID_URL_MESSAGE } from './constants'
import { stopTheSequence } from './utils/stopTheSequence'
import { getAnchorPage } from './page/getAnchorPage'
import { processPageClose, processOverlayClose } from './utils/processClose'

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

    const openingUrl = restore ? compiledUrl : encodeURI(compiledUrl)

    yield openPage(openingUrl, target, pageId)

    stopTheSequence(action)
}

const ROOT_PAGE = '_'

export interface ClosePagePayload {
    pageId: string
    prompt: boolean
}

export const closePageCreator = createAction(
    `${PAGE_PREFIX}close`,
    (payload: ClosePagePayload, meta?: Meta) => ({
        payload,
        meta,
    }),
)

export function* closePageEffect(action: Action<string, ClosePagePayload>) {
    const { payload } = action
    const { pageId, prompt } = payload

    if (pageId === ROOT_PAGE) {
        yield call(processPageClose, pageId, prompt)
    } else {
        yield call(processOverlayClose, pageId, prompt)
    }
}

export function* pagePromptEffect(action: Action<string, ClosePagePayload>) {
    // eslint-disable-next-line no-alert
    if (window.confirm(i18next.t('defaultPromptMessage'))) {
        const { pageId } = action.payload

        yield put(closePageCreator({ pageId, prompt: false }))
    }
}

export const sagas = [
    takeEvery(openPageCreator.type, AsyncEffectWrapper(openPageEffect)),
    takeEvery(closePageCreator.type, AsyncEffectWrapper(closePageEffect)),
    takeEvery(showPagePrompt, AsyncEffectWrapper(pagePromptEffect)),
]
