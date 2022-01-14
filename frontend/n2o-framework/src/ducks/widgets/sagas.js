import {
    put,
    takeEvery,
} from 'redux-saga/effects'

import { PREFIXES } from '../models/constants'
import { setModel } from '../models/store'

import {
    resolveWidget,
} from './store'

export function* runResolve(action) {
    const { modelId, model } = action.payload

    try {
        yield put(setModel(PREFIXES.resolve, modelId, model))
        // eslint-disable-next-line no-empty
    } catch (err) {}
}

export function* clearOnDisable(action) {
    const { modelId } = action.payload

    yield put(setModel(PREFIXES.datasource, modelId, null))
}

/**
 * Сайд-эффекты для виджет редюсера
 * @ignore
 */
export default () => [
    takeEvery(resolveWidget, runResolve),
]
