import { takeEvery, put, select, debounce } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'

import { ModelPrefix } from '../../core/datasource/const'
import { Validation, ValidationsKey } from '../../core/validation/types'
import {
    updateModel,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
    setModel,
} from '../models/store'
import { failValidate, startValidate } from '../datasource/store'
import { FailValidateAction } from '../datasource/Actions'
import { dataSourceValidationSelector } from '../datasource/selectors'

import { makeFormByName, makeFormsByModel } from './selectors'
import {
    setFieldRequired,
    handleBlur,
    handleTouch,
} from './store'
import { Form } from './types'

const validateFields: Record<string, string[]> = {}

const includesField = (entryValue: Validation[], actionField: string) => entryValue[0].on?.some(dependencyField => (
    dependencyField === actionField || // full equality
    dependencyField.startsWith(`${actionField}.`) || // fieldName: "field", on: "field.id"
    actionField.startsWith(`${dependencyField}.`) || // fieldName: "field.inner", on: "field"
    actionField.startsWith(`${dependencyField}[`) // fieldName: "field[index]", on: "field"
))

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
    takeEvery(setModel, function* validateSaga({ payload }) {
        const { prefix, key: datasource, model } = payload

        if (!validateFields[datasource]) {
            validateFields[datasource] = []
        }

        const validation: ReturnType<ReturnType<typeof dataSourceValidationSelector>> = yield select(
            dataSourceValidationSelector(
                datasource,
                prefix === ModelPrefix.filter ? ValidationsKey.FilterValidations : ValidationsKey.Validations,
            ),
        )

        const entries = Object.entries(validation || {})
        const allFields = Object.keys(model || {})

        allFields.forEach((field) => {
            entries.forEach(([key, value]) => {
                if (includesField(value, field)) { validateFields[datasource].push(key) }
            })
        })
    }),
    takeEvery([
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], function* validateSaga({ meta }) {
        const { key: datasource, field, prefix } = meta

        if (!validateFields[datasource]) {
            validateFields[datasource] = []
        }

        const validation: ReturnType<ReturnType<typeof dataSourceValidationSelector>> = yield select(
            dataSourceValidationSelector(
                datasource,
                prefix === ModelPrefix.filter ? ValidationsKey.FilterValidations : ValidationsKey.Validations,
            ),
        )
        const entries = Object.entries(validation || {})
        const fields = [field]

        entries.forEach(([key, value]) => {
            if (includesField(value, field)) { fields.push(key) }
        })

        fields.forEach(field => validateFields[datasource].push(field))

        const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))
        const form: Form = forms?.[0]

        if (isEmpty(form)) {
            // eslint-disable-next-line no-console
            console.warn(`Не найден виджет для формы: ${datasource}`)

            return
        }

        if (!isEmpty(fields)) {
            yield put(
                startValidate(
                    datasource,
                    form.validationKey,
                    prefix,
                    fields,
                    { blurValidation: true, touched: true },
                ),
            )
        }
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
    debounce(200, setModel, function* validateSaga({ payload }) {
        const { prefix, key: datasource } = payload

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
