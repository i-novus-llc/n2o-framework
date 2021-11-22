import {
    put,
    select,
} from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { dataSourceByIdSelector } from '../selectors'
import {
    MODEL_PREFIX,
} from '../../../core/datasource/const'
import { failValidate } from '../store'
import { validateField } from '../../../core/datasource/validateField'

export function* validate({ id, fields }) {
    const state = yield select(dataSourceByIdSelector(id))
    const model = state.models[MODEL_PREFIX.active]
    let entries = Object.entries(state.validation)

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
