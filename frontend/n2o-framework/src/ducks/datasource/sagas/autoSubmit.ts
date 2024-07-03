import { debounce, put, select, takeEvery } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import {
    AppendFieldToArrayAction,
    CopyFieldArrayAction,
    RemoveFieldFromArrayAction,
    UpdateModelAction,
} from '../../models/Actions'
import { DataSourceState } from '../DataSource'
import { dataSourceByIdSelector } from '../selectors'
import { submit } from '../store'
import { appendFieldToArray, copyFieldArray, removeFieldFromArray, updateModel } from '../../models/store'
import { CachedProvider, CachedSubmit, ProviderType, SubmitProvider } from '../Provider'
import { ModelPrefix } from '../../../core/datasource/const'

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
    } else if (datasource.submit?.type === ProviderType.cached) {
        provider = { ...datasource.submit, auto: true }
    } else if (!datasource.submit && datasource.provider?.type === ProviderType.cached) {
        // Костыль для случая, когда не задан submit
        // FIXME: реализовать фабрику для создания разных датасурсов, чтобы не размазывать их логику
        const { key, type, storage } = datasource.provider as CachedProvider

        provider = { key, type, storage, auto: true, model: ModelPrefix.active } as CachedSubmit
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
