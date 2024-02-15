import { debounce, put, select, takeEvery } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import {
    AppendFieldToArrayAction,
    RemoveFieldFromArrayAction,
    CopyFieldArrayAction,
    UpdateModelAction,
} from '../../models/Actions'
import { DataSourceState } from '../DataSource'
import { dataSourceByIdSelector } from '../selectors'
import { submit } from '../store'
import { appendFieldToArray, copyFieldArray, removeFieldFromArray, updateModel } from '../../models/store'
import { SubmitProvider } from '../Provider'

type ModelAction = AppendFieldToArrayAction | RemoveFieldFromArrayAction | CopyFieldArrayAction | UpdateModelAction

let buffer: Record<string, {
    key: string
    provider: SubmitProvider
}> = {}

function* collectSaga({ payload }: ModelAction) {
    const { key, field } = payload
    const datasource: DataSourceState = yield select(dataSourceByIdSelector(key))

    if (isEmpty(datasource)) { return }

    let bufferKey: string = key
    let provider: SubmitProvider | void

    if (datasource.submit?.auto || datasource.submit?.autoSubmitOn) {
        provider = datasource.submit
    } else {
        provider = datasource.fieldsSubmit[field]
        bufferKey = `${key}:${field}`
    }

    if (!isEmpty(provider)) {
        buffer[bufferKey] = { key, provider }
    }
}

function* submitSaga() {
    for (const { key, provider } of Object.values(buffer)) {
        const datasource: DataSourceState = yield select(dataSourceByIdSelector(key))

        if (!isEmpty(datasource)) {
            // @ts-ignore FIXME разобраться почему ругается на количество аргументов
            yield put(submit(key, provider))
        } else {
            // eslint-disable-next-line no-console
            console.warn('Can\'t auto-submit after destroy datasource')
        }
    }

    buffer = {}
}

const pattern = [
    updateModel,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
]

export const autoSubmit = [
    takeEvery(pattern, collectSaga),
    debounce(400, pattern, submitSaga),
]
