import { fork, put, select } from 'redux-saga/effects'
import { Task } from 'redux-saga'
import pick from 'lodash/pick'

import {
    dataSourceErrors,
    dataSourcePageIdSelector,
    dataSourceValidationSelector,
} from '../selectors'
import { getModelSelector, Model } from '../../models/selectors'
import { endValidation } from '../store'
import type { StartValidateAction } from '../Actions'
import { validateFields, validateModel } from '../../../core/validation/validateModel'
import { addFieldMessages } from '../../../core/validation/addFieldMessages'
import { makePageUrlByIdSelector } from '../../pages/selectors'
import { ExtraValidationConfig, Validation, ValidationResult } from '../../../core/validation/types'
import { State } from '../../State'
import { ModelLink, ModelPath, ModelPrefix } from '../../../core/models/types'
import { getModelPath } from '../../../core/models/getModelPath'
import { Errors } from '../DataSource'

type FieldId = string
type Validations = Record<FieldId, Validation[]>
type AsyncValidation = {
    fields: Record<string, Validation[]> | null
    abortController: AbortController
    task: Task
}

const asyncValidations = new Map<ModelPath, AsyncValidation>()

const mergeFields = <T extends Record<string, Validation[]>>(first: T, second: T): T => {
    const result: Partial<T> = {}
    const keys = new Set<keyof T>([
        ...Object.keys(first),
        ...Object.keys(second),
    ])

    keys.forEach((key) => {
        result[key] = [...new Set([...first[key], ...second[key]])] as T[typeof key]
    })

    return result as T
}

const runValidate = async (model: Model, validation: Validations, fields: Validations | null, options: ExtraValidationConfig) => {
    if (fields === null) { return validateModel(model, validation, options) }

    return validateFields(fields, model, options)
}

function* validateSingle(
    modelLink: ModelLink,
    validation: Record<string, Validation[]>,
    options: ExtraValidationConfig,
    fields: Record<string, Validation[]> | null,
    meta: unknown,
) {
    const state: State = yield select()
    const model = (getModelSelector(modelLink)(state) ?? {}) as Model

    const newMessages: Errors = yield runValidate(model, validation, fields, options)
    const keys = fields ? Object.keys(fields) : undefined

    yield put(endValidation({ modelLink, messages: newMessages, fields: keys }, meta))

    const allMessages: Record<string, ValidationResult[]> = yield select(dataSourceErrors(modelLink))
    const withFieldMessages = addFieldMessages(modelLink, allMessages, yield select())

    return keys ? pick(withFieldMessages, keys) : withFieldMessages
}

function* validateMulti(
    modelLink: ModelLink,
    validation: Validations,
    options: ExtraValidationConfig,
    fields: Validations | null,
    meta: unknown,
) {
    const state: State = yield select()
    const model = (getModelSelector(modelLink)(state) || []) as Model[]
    let messages: Errors = {}

    for (let i = 0; i < model.length; i++) {
        const modelMessages: Errors = yield validateSingle(
            { ...modelLink, index: i },
            validation,
            options,
            fields,
            meta,
        )

        messages = {
            ...messages,
            ...Object.fromEntries(Object.entries(modelMessages).map(([key, value]) => [`[${i}]${key}`, value])),
        }
    }

    return messages
}

export function* validate({ payload, meta }: StartValidateAction) {
    const { modelLink, fields } = payload
    const state: State = yield select()
    const { id, prefix, index } = modelLink
    const modelPath = getModelPath(modelLink)
    const validation = dataSourceValidationSelector(id, prefix)(state) || {}

    if (!validation) { return true }

    let fields2Validate = fields ?? null

    if (asyncValidations.has(modelPath)) {
        const prevProcess = asyncValidations.get(modelPath) as AsyncValidation

        fields2Validate = fields2Validate === null || prevProcess.fields === null
            ? null
            : mergeFields(fields2Validate, prevProcess.fields)
        prevProcess.abortController.abort()
        yield prevProcess.task.cancel()
    }

    const abortController = new AbortController()
    const pageId = dataSourcePageIdSelector(id)(state) as string
    const pageUrl = makePageUrlByIdSelector(pageId)(state)
    const options = {
        signal: abortController.signal,
        datasourceId: id,
        pageUrl,
    } as ExtraValidationConfig
    const isMulti = prefix === ModelPrefix.selected || (prefix === ModelPrefix.source && typeof index === 'undefined')
    const currentProcess: AsyncValidation = {
        fields: fields2Validate,
        abortController,
        task: isMulti
            ? yield fork(validateMulti, modelLink, validation, options, fields2Validate, meta)
            : yield fork(validateSingle, modelLink, validation, options, fields2Validate, meta),
    }

    asyncValidations.set(modelPath, currentProcess)

    const messages: Errors = yield currentProcess.task.toPromise()

    asyncValidations.delete(modelPath)

    return messages
}
