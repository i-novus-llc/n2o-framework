import { takeEvery, put, select, debounce } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'

import { State } from '../State'
import { ModelPath, ModelPrefix } from '../../core/models/types'
import { Validation } from '../../core/validation/types'
import {
    updateModel,
    appendToArray,
    removeFromArray,
    copyFieldArray,
    setModel,
} from '../models/store'
import { startValidate, remove as removeDatasource, endValidation } from '../datasource/store'
import { getModelByPrefixAndNameSelector } from '../models/selectors'
import { getCtxFromField, isMulti, createRegexpWithContext } from '../../core/validation/utils'
import { getOnAppend, getOnRemove, mapMultiFields } from '../../core/models/mapMultiFields'
import { getModelPath } from '../../core/models/getModelPath'
import { SetModelAction, UpdateModelAction } from '../models/Actions'
import { dataSourceValidationSelector } from '../datasource/selectors'

import { makeFormByName, makeFormsByModel, makeFormsByModelLink } from './selectors'
import {
    setFieldRequired,
    handleBlur,
} from './store'
import { FieldAction } from './Actions'
import { diffKeys, getDependentSet, pickValidations, unionSets, mapFields } from './helpers'

type FieldId = string
type ValidationKey = string
type BufferValue = Set<Validation> | null

let validateBuffer: Record<
    ModelPath,
    Record<FieldId, BufferValue>
> = {}

const addToBuffer = (
    modelLink: ModelPath,
    field: FieldId,
    allValidations: Record<ValidationKey, Validation[]>,
    fields: string[],
) => {
    const buffer = validateBuffer[modelLink] || {}
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

    validateBuffer[modelLink] = buffer
}

export const formPluginSagas = [
    takeEvery(setModel, function* addFieldToBuffer({ payload, meta }) {
        const { modelLink, model, isDefault } = payload
        const { prefix, id: datasource } = modelLink
        const { validate } = meta
        const state: State = yield select()

        if (isDefault || validate === false) { return }

        const forms = makeFormsByModel(datasource, prefix)(state)

        if (isEmpty(forms)) { return }

        const validations = dataSourceValidationSelector(datasource, prefix)(state) || {}

        if (isEmpty(validations)) { return }

        const prevModel = getModelByPrefixAndNameSelector(prefix, datasource)(
            // @ts-ignore разобраться с типами
            meta.prevState,
        )
        const changedFields = diffKeys(model, prevModel)
        const allFields = Object.keys(forms.map(form => form.fields).reduce((a, b) => ({ ...a, ...b }), {}))

        changedFields.forEach(field => addToBuffer(
            getModelPath({ prefix, id: datasource }),
            field,
            validations,
            allFields,
        ))
    }),
    takeEvery([
        updateModel.type,
        appendToArray.type,
        removeFromArray.type,
        copyFieldArray.type,
    ], function* addFieldToBuffer({ payload, meta = {} }: UpdateModelAction) {
        const { validate } = meta

        if (validate === false) { return }

        const { modelLink, fieldName } = payload
        const { id: datasource, prefix } = modelLink

        if (!fieldName) { return }

        const state: State = yield select()
        const validations = dataSourceValidationSelector(datasource, prefix)(state) || {}

        if (isEmpty(validations)) { return }

        const forms = makeFormsByModelLink(modelLink)(state)
        const allFields = Object.keys(forms.map(form => form.fields).reduce((a, b) => ({ ...a, ...b }), {}))

        addToBuffer(getModelPath(modelLink), fieldName, validations, allFields)
    }),
    takeEvery([
        handleBlur,
        setFieldRequired,
    ], function* addFieldToBuffer({ payload, meta = {} }: FieldAction) {
        const { validate } = meta
        const { formName, fieldName } = payload
        const state: State = yield select()
        const form = makeFormByName(formName)(state)

        if (!form) { return }

        const { modelLink } = form
        const { id: datasource, prefix } = modelLink

        if (validate === false) {
            // Сброс required валидации, даже если в зависимости стоит validate=false
            if (get(payload, 'required') === false) {
                yield put(endValidation({ modelLink, fields: [fieldName], messages: {} }))
            }

            return
        }

        const modelPath = getModelPath(modelLink)

        if (!validateBuffer[modelPath]) { validateBuffer[modelPath] = {} }

        if (!validateBuffer[modelPath][fieldName]) {
            const validations = dataSourceValidationSelector(datasource, prefix)(state) || {}

            validateBuffer[modelPath][fieldName] = pickValidations(validations, fieldName)
        }
    }),
    debounce(200, setModel, function* startValidateSaga({ payload }) {
        const { modelLink } = payload
        const state: State = yield select()

        const [form] = makeFormsByModelLink(modelLink)(state)

        if (!form) { return }

        const modelPath = getModelPath(modelLink)
        const fields = mapFields(validateBuffer[modelPath])

        delete validateBuffer[modelPath]

        if (!isEmpty(fields)) {
            yield put(startValidate(modelLink, fields, { isTriggeredByFieldChange: true }))
        }
    }),
    debounce(200, [
        updateModel,
        appendToArray,
        removeFromArray,
        copyFieldArray,
    ], function* startValidateSaga({ payload }) {
        const { modelLink } = payload

        const state: State = yield select()
        const [form] = makeFormsByModelLink(modelLink)(state)

        if (!form) { return }

        const modelPath = getModelPath(modelLink)
        const fields = mapFields(validateBuffer[modelPath])

        delete validateBuffer[modelPath]

        if (!isEmpty(fields)) {
            yield put(startValidate(modelLink, fields, { isTriggeredByFieldChange: true }))
        }
    }),
    debounce(200, [
        handleBlur,
        setFieldRequired,
    ], function* startValidateSaga({ payload }) {
        const { formName } = payload
        const state: State = yield select()
        const form = makeFormByName(formName)(state)

        if (!form) { return }

        const { modelLink } = form
        const modelPath = getModelPath(modelLink)
        const fields = mapFields(validateBuffer[modelPath])

        delete validateBuffer[modelPath]

        if (!isEmpty(fields)) {
            yield put(startValidate(modelLink, fields, { isTriggeredByFieldChange: true }))
        }
    }),
    takeEvery(removeDatasource, ({ payload }) => {
        delete validateBuffer[getModelPath({ prefix: ModelPrefix.active, id: payload.id })]
        delete validateBuffer[getModelPath({ prefix: ModelPrefix.source, id: payload.id })]
        delete validateBuffer[getModelPath({ prefix: ModelPrefix.edit, id: payload.id })]
        delete validateBuffer[getModelPath({ prefix: ModelPrefix.filter, id: payload.id })]
    }),
    takeEvery(appendToArray, ({ payload }) => {
        const { fieldName, position, modelLink } = payload
        const modelPath = getModelPath(modelLink)

        if (typeof position === 'undefined') { return }

        if (!fieldName) {
            validateBuffer = mapMultiFields(
                validateBuffer,
                modelPath,
                getOnAppend(modelPath, position),
            )

            return
        }

        validateBuffer[modelPath] = mapMultiFields(
            validateBuffer[modelPath] ?? {},
            fieldName,
            getOnAppend(fieldName, position),
        )
    }),
    takeEvery(removeFromArray, ({ payload }) => {
        const { fieldName, start, count = 1, modelLink } = payload
        const modelPath = getModelPath(modelLink)

        if (!fieldName) {
            validateBuffer = mapMultiFields(
                validateBuffer,
                modelPath,
                getOnRemove(modelPath, start, count),
            )

            return
        }

        validateBuffer[modelPath] = mapMultiFields(
            validateBuffer[modelPath] ?? {},
            fieldName,
            getOnRemove(fieldName, start, count),
        )
    }),
    takeEvery(setModel.type, function* setActiveModel({ payload }: SetModelAction<ModelPrefix.source>) {
        const { modelLink, model } = payload
        const { id: key, prefix } = modelLink

        if (prefix !== ModelPrefix.source) { return }

        const state: State = yield select()
        const [form] = [
            ...makeFormsByModelLink({ id: key, prefix: ModelPrefix.active })(state),
            ...makeFormsByModelLink({ id: key, prefix: ModelPrefix.edit })(state),
        ]

        // Костыль, чтобы убрать формы фильтров
        if (!form?.needActiveModel) { return }

        const datasourceModel = model?.[0] || {}
        const resolveModel = getModelByPrefixAndNameSelector(ModelPrefix.active, key)(state)
        const editModel = getModelByPrefixAndNameSelector(ModelPrefix.edit, key)(state)
        const { modelLink: { prefix: formPrefix } } = form

        // FIXME: Удалить костыль с добалением resolveModel если нет editModel, после удаления edit из редюсера models
        const activeModel = formPrefix === ModelPrefix.edit
            ? (editModel || resolveModel)
            : resolveModel

        const initialValues = isEmpty(activeModel) && isEmpty(datasourceModel)
            // Возвращение null необходимо, поскольку если вернуть undefined redux-toolkit не вызовет экшен
            ? null
            : { ...activeModel, ...datasourceModel }

        if (formPrefix === ModelPrefix.edit) {
            yield put(setModel({ prefix: ModelPrefix.edit, id: key }, initialValues, true))
        }

        yield put(setModel({ prefix: ModelPrefix.active, id: key }, initialValues, true))
    }),
]
