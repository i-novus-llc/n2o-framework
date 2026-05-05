import { fork, put, select } from 'redux-saga/effects'
import { Task } from 'redux-saga'
import pick from 'lodash/pick'

import {
    dataSourceErrors,
    dataSourcePageIdSelector,
    dataSourceValidationSelector,
} from '../selectors'
import { getModelByPrefixAndNameSelector, Model } from '../../models/selectors'
import { endValidation } from '../store'
import type { StartValidateAction } from '../Actions'
import { validateFields, validateModel } from '../../../core/validation/validateModel'
import { addFieldMessages } from '../../../core/validation/addFieldMessages'
import { makePageUrlByIdSelector } from '../../pages/selectors'
import { Validation, ValidationResult } from '../../../core/validation/types'

type FieldId = string
type Validations = Record<FieldId, Validation[]>
type AsyncValidation = {
    fields: Validations | null
    abortController: AbortController
    task: Task
}

const asyncValidations: Record<string, AsyncValidation | null> = {}

const mergeFields = <
    T extends Record<string, Validation[]>,
>(first: T, second: T) => {
    const result: Record<string, Validation[]> = {}
    const keys = new Set([
        ...Object.keys(first),
        ...Object.keys(second),
    ])

    keys.forEach((key) => {
        result[key] = [...new Set([...first[key], ...second[key]])]
    })

    return result
}

export function* validate({ payload, meta }: StartValidateAction) {
    const { id, validationsKey, prefix, fields } = payload
    const validation: ReturnType<ReturnType<typeof dataSourceValidationSelector>> =
        yield select(dataSourceValidationSelector(id, validationsKey))

    if (!validation) { return true }

    const prevProcess = asyncValidations[id]
    let fields2Validate = fields ?? null

    if (prevProcess) {
        fields2Validate = fields2Validate === null || prevProcess.fields === null
            ? null
            : mergeFields(fields2Validate, prevProcess.fields)
        prevProcess.abortController.abort()
        yield prevProcess.task.cancel()
    }

    const model: Model = yield select(getModelByPrefixAndNameSelector(prefix, id))
    const abortController = new AbortController()
    const pageId: string = yield select(dataSourcePageIdSelector(id))
    const pageUrl: string = yield select(makePageUrlByIdSelector(pageId))
    const options = {
        signal: abortController.signal,
        datasourceId: id,
        pageUrl,
    }

    const currentProcess: AsyncValidation = {
        fields: fields2Validate,
        abortController,
        task: fields2Validate === null
            ? yield fork(validateModel, model, validation, options)
            : yield fork(validateFields, fields2Validate, model, options),
    }

    asyncValidations[id] = currentProcess

    const modelMessages: Awaited<ReturnType<typeof validateModel>> = yield currentProcess.task.toPromise()
    const keys = fields2Validate ? Object.keys(fields2Validate) : undefined

    yield put(endValidation({ id, messages: modelMessages, prefix, fields: keys }, meta))

    asyncValidations[id] = null

    const messages: Record<string, ValidationResult[]> = yield select(dataSourceErrors(id, prefix))
    const newMessages = addFieldMessages(id, messages, yield select())

    return keys ? pick(newMessages, keys) : newMessages
}
