import { put, select, delay } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { dataSourceModelByPrefixSelector, dataSourceValidationSelector } from '../selectors'
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

    // TODO удалить после рефакторинга форм
    // после blur валидация срабатывает раньше, чем сетится модель, поэтому тут временный костылек
    yield delay(16)
    const model: ReturnType<typeof dataSourceModelByPrefixSelector> =
        yield select(dataSourceModelByPrefixSelector(id, prefix))

    // @ts-ignore поправить типы
    yield put(resetValidation(id, fields, prefix))

    const messages: Awaited<ReturnType<typeof validateModel>> = yield validateModel(model, validation, fields)

    if (!isEmpty(messages)) {
        // @ts-ignore поправить типы
        yield put(failValidate(id, messages, prefix, meta))
    }

    return !hasError(messages)
}
