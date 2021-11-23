import {
    put,
    select,
} from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { dataSourceModelsSelector, dataSourceValidationSelector } from '../selectors'
import {
    MODEL_PREFIX,
} from '../../../core/datasource/const'
import { failValidate } from '../store'
import { validateField } from '../../../core/datasource/validateField'

export function* validate({ payload }) {
    const { id, fields } = { payload }
    const validation = yield select(dataSourceValidationSelector(id))
    const models = yield select(dataSourceModelsSelector(id))
    const model = models[MODEL_PREFIX.active]
    let entries = Object.entries(validation)

    if (fields?.length) {
        entries = entries.filter(([field]) => fields.includes(field))
    }

    const errors = {}

    entries.forEach(([field, validationList]) => {
        errors[field] = validateField(field, model, validationList || [])
    })

    if (!isEmpty(errors)) {
        yield put(failValidate(id, errors))
    }
}
