import { put, select, delay } from 'redux-saga/effects'
import { isEmpty, isEqual } from 'lodash'

import { dataSourceErrors, dataSourceModelByPrefixSelector, dataSourceValidationSelector } from '../selectors'
import { failValidate, resetValidation } from '../store'
import type { StartValidateAction } from '../Actions'
import { hasError, validateModel } from '../../../core/validation/validateModel'

export function* validate({ payload, meta }: StartValidateAction) {
    const { id, validationsKey, prefix, fields = [] } = payload
    const validation: ReturnType<ReturnType<typeof dataSourceValidationSelector>> =
        yield select(dataSourceValidationSelector(id, validationsKey))

    if (!validation) {
        return false
    }

    const prevMessages: ReturnType<typeof dataSourceErrors> =
        yield select(dataSourceErrors(id, prefix))

    // TODO удалить после рефакторинга форм
    // после blur валидация срабатывает раньше, чем сетится модель, поэтому тут временный костылек
    yield delay(16)

    const model: ReturnType<typeof dataSourceModelByPrefixSelector> =
        yield select(dataSourceModelByPrefixSelector(id, prefix))
    const messages: Awaited<ReturnType<typeof validateModel>> = yield validateModel(model, validation, fields)
    const fieldsToReset = Object.keys(prevMessages).filter(field => isEmpty(messages[field]))

    if (!isEmpty(fieldsToReset)) {
        // @ts-ignore поправить типы
        yield put(resetValidation(id, fields, prefix))
    }

    if (!isEmpty(messages) && !isEqual(messages, prevMessages)) {
        // @ts-ignore поправить типы
        yield put(failValidate(id, messages, prefix, meta))
    }

    return !hasError(messages)
}
