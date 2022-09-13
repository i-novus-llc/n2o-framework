import {
    put,
    select,
} from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { dataSourceModelsSelector, dataSourceValidationSelector } from '../selectors'
import { failValidate, resetValidation } from '../store'
import type { StartValidateAction } from '../Actions'
import { hasError, validateModel } from '../../../core/validation/validateModel'

export function* validate({ payload, meta }: StartValidateAction) {
    const { id, fields, prefix } = payload
    let validation: ReturnType<ReturnType<typeof dataSourceValidationSelector>> =
        yield select(dataSourceValidationSelector(id))

    if (!validation) {
        return false
    }

    const models: ReturnType<ReturnType<typeof dataSourceModelsSelector>> = yield select(dataSourceModelsSelector(id))
    const model = models[prefix] || {}

    yield put(resetValidation(id, fields, prefix))

    if (fields?.length) {
        validation = Object.fromEntries(
            Object.entries(validation)
                .filter(([key]) => fields.includes(key)),
        )
    }

    const messages: Awaited<ReturnType<typeof validateModel>> = yield validateModel(model, validation)

    if (!isEmpty(messages)) {
        yield put(failValidate(id, messages, prefix, meta))
    }

    return !hasError(messages)
}
