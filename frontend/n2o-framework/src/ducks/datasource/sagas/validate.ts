import { put, select, delay } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { dataSourceModelsSelector, dataSourceValidationSelector } from '../selectors'
import { failValidate, resetValidation } from '../store'
import type { StartValidateAction } from '../Actions'
import { hasError, validateModel } from '../../../core/validation/validateModel'
// @ts-ignore import from js file
import { getFormFieldsByName } from '../../form/selectors'

export function* validate({ payload, meta }: StartValidateAction) {
    const { id, validationsKey, prefix, fields = [] } = payload
    const formFields: Record<string, { touched: boolean }> = yield select(getFormFieldsByName(id))
    let validation: ReturnType<ReturnType<typeof dataSourceValidationSelector>> =
        yield select(dataSourceValidationSelector(id, validationsKey))

    if (!validation) {
        return false
    }

    // TODO удалить после рефакторинга форм
    // после blur валидация срабатывает раньше, чем сетится модель, поэтому тут временный костылек
    yield delay(16)
    const models: ReturnType<ReturnType<typeof dataSourceModelsSelector>> = yield select(dataSourceModelsSelector(id))
    const model = models[prefix] || {}

    yield put(resetValidation(id, fields, prefix))

    if (fields?.length) {
        validation = Object.fromEntries(
            Object.entries(validation)
                .filter(([key]) => fields.includes(key) && (meta?.touched || formFields[key]?.touched)),
        )
    }

    const messages: Awaited<ReturnType<typeof validateModel>> = yield validateModel(model, validation)

    if (!isEmpty(messages)) {
        yield put(failValidate(id, messages, prefix, meta))
    }

    return !hasError(messages)
}
