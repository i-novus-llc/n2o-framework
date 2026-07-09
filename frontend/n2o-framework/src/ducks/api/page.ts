import { put, select, takeEvery, call } from 'redux-saga/effects'
import { createAction } from '@reduxjs/toolkit'
import { push, getLocation } from 'connected-react-router'
import get from 'lodash/get'
import isUndefined from 'lodash/isUndefined'
import i18next from 'i18next'

import { type Action, type Meta } from '../Action'
import { type State as GlobalState } from '../State'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { executeExpression } from '../../core/Expression/execute'
import { parseExpression } from '../../core/Expression/parse'
import { setLocation, showPagePrompt } from '../pages/store'
import { N2OLinkTarget } from '../../components/core/router/types'
import { prepareLink } from '../../components/core/router/utils/prepareLink'
import { makePageUrlByIdSelector } from '../pages/selectors'

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
    target: N2OLinkTarget
    newWindow?: boolean
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
    { href, path }: { href: string, path?: string },
    newWindow?: boolean,
    pageId?: string,
) {
    if (newWindow) {
        window.open(href)

        return
    }
    if (!path) {
        window.location.href = href

        return
    }

    const state: GlobalState = yield select()
    const anchorPageId = getAnchorPage(path, state, pageId)

    if (anchorPageId) {
        yield put(setLocation(pageId, path))
    } else {
        yield put(push(path))
    }
}

export function* openPageEffect(action: Action<string, OpenPagePayload>) {
    const { payload, meta = {} } = action
    const state: GlobalState = yield select()
    const { pathname }: Location = yield select(getLocation)
    const { url, pathMapping, queryMapping, target, modelLink, restore = false, newWindow } = payload
    const { evalContext, pageId } = meta
    const pageUrl = pageId ? makePageUrlByIdSelector(pageId)(state) : undefined
    const baseUrl = pageUrl || pathname

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

    if (typeof compiledUrl !== 'string') { throw new Error('Compiled URL must be a string') }

    const openingUrl = prepareLink(restore ? compiledUrl : encodeURI(compiledUrl), target, baseUrl)

    yield openPage(openingUrl, newWindow, pageId)

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
