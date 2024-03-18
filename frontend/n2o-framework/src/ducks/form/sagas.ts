import { takeEvery, put, select, debounce } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import { isEqual } from 'lodash'

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
import { getModelByPrefixAndNameSelector } from '../models/selectors'

import { makeFormByName, makeFormsByModel } from './selectors'
import {
    setFieldRequired,
    handleBlur,
    handleTouch,
} from './store'
import { Form } from './types'

const validateFields: Record<string, string[]> = {}

const includesField = (validations: Validation[], actionField: string) => validations.some(
    validation => validation.on?.some(
        dependencyField => (
            dependencyField === actionField || // full equality
            dependencyField.startsWith(`${actionField}.`) || // fieldName: "field", on: "field.id"
            actionField.startsWith(`${dependencyField}.`) || // fieldName: "field.inner", on: "field"
            actionField.startsWith(`${dependencyField}[`) // fieldName: "field[index]", on: "field"
        ),
    ),
)

function diffKeys <
    TValue extends Record<string, unknown> | undefined | null
>(first: TValue, second: TValue) {
    if (!first || !second) {
        if (first) { return Object.keys(first) }
        if (second) { return Object.keys(second) }

        return []
    }

    return [...new Set([
        ...Object.keys(first),
        ...Object.keys(second),
    ])].filter(key => !isEqual(first[key], second[key]))
}

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
    takeEvery(setModel, function* addFieldToBuffer({ payload, meta }) {
        const { prefix, key: datasource, model, isDefault } = payload

        if (isDefault) {
            return
        }
        if (prefix === ModelPrefix.source || prefix === ModelPrefix.selected) {
            return
        }

        const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))
        const form: Form = forms?.[0]

        if (isEmpty(form)) { return }

        const prevModel = getModelByPrefixAndNameSelector(prefix, datasource)(
            // @ts-ignore разобраться с типами
            meta.prevState,
        ) as Record<string, unknown> | null
        // @ts-ignore разобраться с типами
        const fields = diffKeys(model, prevModel)

        if (!validateFields[datasource]) {
            validateFields[datasource] = []
        }

        const allValidations: ReturnType<ReturnType<typeof dataSourceValidationSelector>> = yield select(
            dataSourceValidationSelector(
                datasource,
                prefix === ModelPrefix.filter ? ValidationsKey.FilterValidations : ValidationsKey.Validations,
            ),
        )

        fields.forEach((field) => {
            if (!validateFields[datasource].includes(field)) {
                validateFields[datasource].push(field)
            }

            for (const [fieldName, validations] of Object.entries(allValidations || {})) {
                if (
                    includesField(validations, field) &&
                    !validateFields[datasource].includes(fieldName)
                ) {
                    validateFields[datasource].push(fieldName)
                }
            }
        })
    }),
    takeEvery([
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], function* addFieldToBuffer({ meta }) {
        const { key: datasource, field, prefix } = meta

        if (!validateFields[datasource]) {
            validateFields[datasource] = []
        }

        if (!validateFields[datasource].includes(field)) {
            validateFields[datasource].push(field)
        }

        const allValidations: ReturnType<ReturnType<typeof dataSourceValidationSelector>> = yield select(
            dataSourceValidationSelector(
                datasource,
                prefix === ModelPrefix.filter ? ValidationsKey.FilterValidations : ValidationsKey.Validations,
            ),
        )

        for (const [fieldName, validations] of Object.entries(allValidations || {})) {
            if (
                includesField(validations, field) &&
                !validateFields[datasource].includes(fieldName)
            ) {
                validateFields[datasource].push(fieldName)
            }
        }
    }),
    takeEvery([
        handleBlur,
        setFieldRequired,
    ], function* addFieldToBuffer({ payload }) {
        const { formName, fieldName } = payload
        const { datasource }: Form = yield select(makeFormByName(formName))

        if (!validateFields[datasource]) {
            validateFields[datasource] = []
        }

        if (!validateFields[datasource].includes(fieldName)) {
            validateFields[datasource].push(fieldName)
        }
    }),
    debounce(200, setModel, function* startValidateSaga({ payload }) {
        const { prefix, key: datasource } = payload

        const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))
        const form: Form = forms?.[0]

        if (isEmpty(form)) { return }

        const fields = validateFields[datasource]

        delete validateFields[datasource]

        if (!isEmpty(fields)) {
            // @ts-ignore FIXME разобраться TS2554: Expected 1 arguments, but got 5
            yield put(startValidate(datasource, form.validationKey, prefix, fields, { blurValidation: true }))
        }
    }),
    debounce(200, [
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], function* startValidateSaga({ meta }) {
        const { key: datasource, prefix } = meta
        const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))
        const form: Form = forms?.[0]

        if (isEmpty(form)) { return }

        const fields = validateFields[datasource]

        delete validateFields[datasource]

        if (!isEmpty(fields)) {
            // @ts-ignore FIXME разобраться TS2554: Expected 1 arguments, but got 5
            yield put(startValidate(datasource, form.validationKey, prefix, fields, { blurValidation: true }))
        }
    }),
    debounce(200, [
        handleBlur,
        setFieldRequired,
    ], function* startValidateSaga({ payload }) {
        const { formName } = payload
        const { datasource, modelPrefix, validationKey } = yield select(makeFormByName(formName))

        const fields = validateFields[datasource]

        delete validateFields[datasource]

        if (!isEmpty(fields)) {
            // @ts-ignore FIXME разобраться TS2554: Expected 1 arguments, but got 5
            yield put(startValidate(datasource, validationKey, modelPrefix, fields, { blurValidation: true }))
        }
    }),
]
