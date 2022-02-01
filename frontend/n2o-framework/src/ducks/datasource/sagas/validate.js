import {
    put,
    select,
} from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { dataSourceModelsSelector, dataSourceValidationSelector } from '../selectors'
import {
    VALIDATION_SEVERITY,
} from '../../../core/datasource/const'
import { failValidate } from '../store'
import { validateField } from '../../../core/datasource/validateField'

export function* validate({ payload, meta }) {
    const { id, fields, prefix } = payload
    const validation = yield select(dataSourceValidationSelector(id))
    const models = yield select(dataSourceModelsSelector(id))
    const model = models[prefix] || {}
    let entries = Object.entries(validation)

    if (fields?.length) {
        entries = entries.filter(([field]) => fields.includes(field))
    }

    const allMessages = {}

    for (const [field, validationList] of entries) {
        const messages = yield validateField(field, model, validationList || [])

        if (messages?.length) {
            allMessages[field] = messages
        }
    }

    const invalid = Object.values(allMessages).some(messages => messages.some(message => (
        message.severity === VALIDATION_SEVERITY.danger || message.severity === VALIDATION_SEVERITY.warning
    )))

    if (!isEmpty(allMessages)) {
        yield put(failValidate(id, allMessages, prefix, meta))
    }

    return !invalid
}
