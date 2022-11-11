import { put, select, takeEvery } from 'redux-saga/effects'
import { createAction } from '@reduxjs/toolkit'
import { push } from 'connected-react-router'

import { Action, Meta } from '../Action'
import { State as GlobalState } from '../State'
// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../../core/dataProviderResolver'

import { PAGE_PREFIX } from './constants'

export type OpenPagePayload = {
    url: string
    pathMapping: object
    queryMapping: object
    target?: '_blank' | 'application'
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

    const { url, pathMapping, queryMapping, target } = payload
    const { url: compiledUrl } = dataProviderResolver(state, {
        url,
        pathMapping,
        queryMapping,
    })

    if (target === 'application') {
        yield put(push(compiledUrl))
    } else if (target === '_blank') {
        window.open(compiledUrl)
    } else {
        window.location = compiledUrl
    }
}

export const sagas = [
    takeEvery(openPagecreator.type, openPageEffect),
]
