import { debounce, put, select, takeEvery } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import {
    AppendFieldToArrayAction,
    CopyFieldArrayAction,
    RemoveFieldFromArrayAction,
    SetModelAction,
    UpdateModelAction,
} from '../../models/Actions'
import { DataSourceState } from '../DataSource'
import { dataSourceByIdSelector } from '../selectors'
import { submit } from '../store'
import { appendFieldToArray, copyFieldArray, removeFieldFromArray, setModel, updateModel } from '../../models/store'
import { CachedAutoSubmit, CachedProvider, ProviderType, SubmitProvider } from '../Provider'
import { ModelPrefix } from '../../../core/datasource/const'

type ModelAction = AppendFieldToArrayAction | RemoveFieldFromArrayAction | CopyFieldArrayAction | UpdateModelAction

let buffer: Record<string, {
    key: string
    provider: SubmitProvider
}> = {}

function* collectFormUpdates({ payload }: ModelAction) {
    const {
        key,
        field,
        prefix,
    } = payload

    if (prefix === ModelPrefix.filter) { return }

    const datasource: DataSourceState = yield select(dataSourceByIdSelector(key))

    if (isEmpty(datasource)) { return }

    let bufferKey: string = key
    let provider: SubmitProvider | void

    if (datasource.submit?.auto || datasource.submit?.autoSubmitOn) {
        provider = datasource.submit
    } else if (datasource.provider?.type === ProviderType.cached) {
        const { key, storage } = datasource.provider as CachedProvider

        provider = {
            key,
            type: ProviderType.autoSaveCache,
            storage,
            model: prefix || ModelPrefix.active,
        } as CachedAutoSubmit
    } else {
        provider = datasource.fieldsSubmit[field]
        bufferKey = `${key}:${field}`
    }

    if (!isEmpty(provider)) {
        buffer[bufferKey] = { key, provider }
    }
}

function* collectSetModel({ payload }: SetModelAction) {
    const {
        key,
        prefix,
        isDefault,
    } = payload

    if (
        isDefault ||
        prefix === ModelPrefix.filter ||
        prefix === ModelPrefix.selected
    ) { return }

    const datasource: DataSourceState = yield select(dataSourceByIdSelector(key))

    if (datasource?.provider?.type !== ProviderType.cached) { return }

    const { key: storageKey, storage } = datasource.provider as CachedProvider

    const provider = {
        key: storageKey,
        type: ProviderType.autoSaveCache,
        storage,
        model: prefix,
    } as CachedAutoSubmit // FIXME: Поправить тип базового submitProvider - убрать в нём лишние поля

    buffer[key] = buffer[key] || { key, provider }
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

const updatePattern = [
    updateModel,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
]

export const autoSubmit = [
    takeEvery(updatePattern, collectFormUpdates),
    takeEvery(setModel.type, collectSetModel),
    debounce(400, [...updatePattern, setModel.type], submitSaga),
]
