import { takeEvery, put, select, debounce } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'

import {
    updateModel,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
} from '../models/store'
import { failValidate, startValidate } from '../datasource/store'
import { FailValidateAction } from '../datasource/Actions'

import { makeFormByName, makeFormsByModel } from './selectors'
import {
    setFieldRequired,
    handleBlur,
    handleTouch,
} from './store'
import { Form } from './types'

const validateFields: Record<string, string[]> = {}

export const formPluginSagas = [
    takeEvery(failValidate, function* touchOnFailValidate({ payload, meta }: FailValidateAction) {
        if (!meta?.touched) { return }

        const { prefix, id: datasource, fields } = payload
        const keys = Object.keys(fields)
        const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))

        for (const form of forms) {
            yield put(handleTouch(form.formName, keys))
        }
    }),
    takeEvery([
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], ({ meta }) => {
        const { key: datasource, field } = meta

        if (!validateFields[datasource]) {
            validateFields[datasource] = []
        }

        validateFields[datasource].push(field)
    }),
    takeEvery([
        handleBlur,
        setFieldRequired,
    ], function* validateSaga({ payload }) {
        const { formName, fieldName } = payload
        const { datasource }: Form = yield select(makeFormByName(formName))

        if (!validateFields[datasource]) {
            validateFields[datasource] = []
        }

        validateFields[datasource].push(fieldName)
    }),
    debounce(200, [
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], function* validateSaga({ meta }) {
        const { key: datasource, prefix } = meta
        const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))
        const form: Form = forms?.[0]

        if (isEmpty(form)) {
        // eslint-disable-next-line no-console
            console.warn(`Не найден виджет для формы: ${datasource}`)

            return
        }

        const fields = validateFields[datasource]

        delete validateFields[datasource]

        if (!isEmpty(fields)) {
            yield put(
                startValidate(
                    datasource,
                    form.validationKey,
                    prefix,
                    fields,
                    { blurValidation: true },
                ),
            )
        }
    }),
    debounce(200, [
        handleBlur,
        setFieldRequired,
    ], function* validateSaga({ payload }) {
        const { formName } = payload
        const { datasource, modelPrefix, validationKey } = yield select(makeFormByName(formName))

        const fields = validateFields[datasource]

        delete validateFields[datasource]

        if (!isEmpty(fields)) {
            yield put(
                startValidate(
                    datasource,
                    validationKey,
                    modelPrefix,
                    fields,
                    { blurValidation: true },
                ),
            )
        }
    }),
]
