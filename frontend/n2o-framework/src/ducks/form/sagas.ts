import { takeEvery, put, select, debounce } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'

import { ModelPrefix } from '../../core/datasource/const'
import { Validation } from '../../core/validation/types'
import {
    updateModel,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
    setModel,
} from '../models/store'
import { resetValidation, startValidate, remove as removeDatasource } from '../datasource/store'
import { dataSourceValidationSelector } from '../datasource/selectors'
import { getModelByPrefixAndNameSelector } from '../models/selectors'
import { getCtxFromField, isMulti, createRegexpWithContext } from '../../core/validation/utils'

import { makeFormByName, makeFormsByModel } from './selectors'
import {
    setFieldRequired,
    handleBlur,
} from './store'
import { Form } from './types'
import { FieldAction } from './Actions'
import { diffKeys, getDependentSet, getValidationFields, pickValidations, unionSets, mapFields } from './helpers'

type DataSourceId = string
type FieldId = string
type ValidationKey = string

const validateBuffer: Record<
    string,
    Record<FieldId, Set<Validation> | null>
> = {}

const addToBuffer = (
    datasource: DataSourceId,
    field: FieldId,
    allValidations: Record<ValidationKey, Validation[]>,
    fields: string[],
) => {
    const buffer = validateBuffer[datasource] || {}
    const ctx = getCtxFromField(field)

    buffer[field] = buffer[field] || pickValidations(allValidations, field)

    for (const [validationKey, validations] of Object.entries(allValidations || {})) {
        const dependent = getDependentSet(validations, field)

        if (!dependent.size) { continue }
        if (!isMulti(validationKey)) {
            buffer[validationKey] = unionSets(dependent, buffer[validationKey]) // validationKey equal fieldId

            continue
        }

        const mask = createRegexpWithContext(validationKey, ctx)

        fields.filter(field => mask.test(field)).forEach((fieldName) => {
            buffer[fieldName] = unionSets(dependent, buffer[fieldName])
        })
    }

    validateBuffer[datasource] = buffer
}

export const formPluginSagas = [
    takeEvery(setModel, function* addFieldToBuffer({ payload, meta }) {
        const { prefix, key: datasource, model, isDefault } = payload
        const { validate } = meta

        //  FIXME: разобраться что не так с типами meta И почему он "validate?: boolean" принимает за "validate: boolean"
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-boolean-literal-compare
        if (isDefault || validate === false) { return }
        if (prefix === ModelPrefix.source || prefix === ModelPrefix.selected) { return }

        const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))
        const form: Form = forms?.[0]

        if (isEmpty(form)) { return }

        const validations: ReturnType<ReturnType<typeof dataSourceValidationSelector>> = yield select(
            state => getValidationFields(state, datasource),
        )

        if (isEmpty(validations)) { return }

        const prevModel = getModelByPrefixAndNameSelector(prefix, datasource)(
            // @ts-ignore разобраться с типами
            meta.prevState,
        ) as Record<string, unknown> | null
        // @ts-ignore разобраться с типами
        const changedFields = diffKeys(model, prevModel)
        const allFields = Object.keys(forms.map(form => form.fields).reduce((a, b) => ({ ...a, ...b }), {}))

        changedFields.forEach(field => addToBuffer(datasource, field, validations, allFields))
    }),
    takeEvery([
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], function* addFieldToBuffer({ payload, meta }) {
        //  FIXME: разобраться что не так с типами meta
        const { validate } = meta

        if (validate === false) { return }

        const { key: datasource, field, prefix } = payload

        const validations: ReturnType<ReturnType<typeof dataSourceValidationSelector>> = yield select(
            state => getValidationFields(state, datasource),
        )

        if (isEmpty(validations)) { return }

        const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))
        const allFields = Object.keys(forms.map(form => form.fields).reduce((a, b) => ({ ...a, ...b }), {}))

        addToBuffer(datasource, field, validations, allFields)
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

        if (!validateBuffer[datasource]) { validateBuffer[datasource] = {} }

        if (!validateBuffer[datasource][fieldName]) {
            const validations: Record<ValidationKey, Validation[]> = yield select(
                state => getValidationFields(state, datasource),
            )

            validateBuffer[datasource][fieldName] = pickValidations(validations, fieldName)
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

        const fields = mapFields(validateBuffer[datasource])

        delete validateBuffer[datasource]

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

        const fields = mapFields(validateBuffer[datasource])

        delete validateBuffer[datasource]

        if (!isEmpty(fields)) {
            yield put(startValidate(datasource, validationKey, modelPrefix, fields, { isTriggeredByFieldChange: true }))
        }
    }),
    takeEvery(removeDatasource, ({ payload }) => { delete validateBuffer[payload.id] }),
]
