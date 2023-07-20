import {
    put,
    takeEvery,
} from 'redux-saga/effects'

import { ModelPrefix } from '../../core/datasource/const'
import { setModel } from '../models/store'

import {
    resolveWidget,
} from './store'
import { Resolve } from './Actions'

export function* runResolve(action: Resolve) {
    const { modelId, model } = action.payload

    try {
        yield put(setModel(ModelPrefix.active, modelId, model))
    } catch (err) {
        /**/
    }
}

/**
 * Сайд-эффекты для виджет редюсера
 * @ignore
 */
export default () => [
    takeEvery(resolveWidget, runResolve),
]
