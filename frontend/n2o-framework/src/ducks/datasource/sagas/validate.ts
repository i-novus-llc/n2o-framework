import {
    put,
    select,
} from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { dataSourceModelsSelector, dataSourceValidationSelector } from '../selectors'
import { failValidate } from '../store.js'
import { StartValidateAction } from '../Actions'
import { hasError, validateModel } from '../../../core/validation/validateModel'

export function* validate({ payload, meta }: StartValidateAction) {
    const { id, fields, prefix } = payload
    let validation = (yield select(dataSourceValidationSelector(id))) as ReturnType<ReturnType<typeof dataSourceValidationSelector>>
    const models = (yield select(dataSourceModelsSelector(id))) as ReturnType<ReturnType<typeof dataSourceModelsSelector>>
    const model = models[prefix] || {}

    if (fields?.length) {
        validation = Object.fromEntries(
            Object.entries(validation)
            .filter(([key]) => fields.includes(key))
        )
    }

    const messages = (yield validateModel(model, validation)) as Awaited<ReturnType<typeof validateModel>>

    if (!isEmpty(messages)) {
        yield put(failValidate(id, messages, prefix, meta))
    }

    return !hasError(messages)
}
