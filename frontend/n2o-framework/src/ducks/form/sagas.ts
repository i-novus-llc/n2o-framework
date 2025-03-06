import { takeEvery, put, select, debounce } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'

import { ModelPrefix } from '../../core/datasource/const'
import { Validation, ValidationsKey } from '../../core/validation/types'
import {
    updateModel,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
    setModel,
} from '../models/store'
import { resetValidation, startValidate } from '../datasource/store'
import { dataSourceValidationSelector } from '../datasource/selectors'
import { getModelByPrefixAndNameSelector } from '../models/selectors'
import { State } from '../State'

import { makeFormByName, makeFormsByModel } from './selectors'
import {
    setFieldRequired,
    handleBlur,
} from './store'
import { Form } from './types'
import { FieldAction } from './Actions'

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

const getValidationFields = (state: State, id: string) => {
    const filterValidation = dataSourceValidationSelector(id, ValidationsKey.FilterValidations)(state) || {}
    const validation = dataSourceValidationSelector(id, ValidationsKey.Validations)(state) || {}

    return Object.keys(filterValidation).length ? filterValidation : validation
}

function diffKeys <
    TValue extends Record<string, unknown> | undefined | null,
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
    takeEvery(setModel, function* addFieldToBuffer({ payload, meta }) {
        const { prefix, key: datasource, model, isDefault } = payload
        const { validate } = meta

        if (isDefault || validate === false) { return }
        if (prefix === ModelPrefix.source || prefix === ModelPrefix.selected) { return }

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
            state => getValidationFields(state, datasource),
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
    ], function* addFieldToBuffer({ payload, meta }) {
        const { validate } = meta

        if (validate === false) { return }

        const { key: datasource, field } = payload

        if (!validateFields[datasource]) {
            validateFields[datasource] = []
        }

        if (!validateFields[datasource].includes(field)) {
            validateFields[datasource].push(field)
        }

        const allValidations: ReturnType<ReturnType<typeof dataSourceValidationSelector>> = yield select(
            state => getValidationFields(state, datasource),
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
    ], function* addFieldToBuffer({ payload, meta = {} }: FieldAction) {
        const { validate } = meta

        if (validate === false) {
            const { formName, fieldName } = payload

            // Сброс required валидации, даже если в зависимости стоит validate=false
            if (get(payload, 'required') !== false) {
                const { modelPrefix } = yield select(makeFormByName(formName))

                yield put(resetValidation(formName, [fieldName], modelPrefix))
            }

            return
        }

        const { formName, fieldName } = payload
        const { datasource }: Form = yield select(makeFormByName(formName))

        if (!validateFields[datasource]) { validateFields[datasource] = [] }

        if (!validateFields[datasource].includes(fieldName)) {
            validateFields[datasource].push(fieldName)
        }
    }),
    debounce(200, [
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
        setModel,
    ], function* startValidateSaga({ payload }) {
        const { prefix, key: datasource } = payload

        const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))
        const form: Form = forms?.[0]

        if (isEmpty(form)) { return }

        const fields = validateFields[datasource]

        delete validateFields[datasource]

        if (!isEmpty(fields)) {
            yield put(startValidate(datasource, form.validationKey, prefix, fields, { isTriggeredByFieldChange: true }))
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
            yield put(startValidate(datasource, validationKey, modelPrefix, fields, { isTriggeredByFieldChange: true }))
        }
    }),
]
