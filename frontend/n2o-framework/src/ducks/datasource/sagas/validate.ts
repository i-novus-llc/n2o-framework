import { put, select, delay, fork } from 'redux-saga/effects'
import { isEmpty, isEqual, pick } from 'lodash'
import { Task } from 'redux-saga'

import { dataSourceErrors, dataSourceModelByPrefixSelector, dataSourceValidationSelector, dataSourcePageIdSelector } from '../selectors'
import { failValidate, resetValidation } from '../store'
import type { StartValidateAction } from '../Actions'
import { hasError, validateModel } from '../../../core/validation/validateModel'
import { makePageUrlByIdSelector } from '../../pages/selectors'

type AsyncValidation = {
    fields: string[]
    abortController: AbortController
    task: Task
}

const asyncValidations: Record<string, AsyncValidation | null> = {}

export function* validate({ payload, meta }: StartValidateAction) {
    const { id, validationsKey, prefix, fields } = payload
    const validation: ReturnType<ReturnType<typeof dataSourceValidationSelector>> =
        yield select(dataSourceValidationSelector(id, validationsKey))

    if (!validation) {
        return false
    }

    const prevProcess = asyncValidations[id]
    let fields2Validate = fields?.length ? fields : Object.keys(validation)

    if (prevProcess) {
        fields2Validate = [...new Set([
            ...fields2Validate,
            ...prevProcess.fields,
        ])]
        prevProcess.abortController.abort()
        yield prevProcess.task.cancel()
    }
    const allMessages: ReturnType<ReturnType<typeof dataSourceErrors>> =
            yield select(dataSourceErrors(id, prefix))
    const fieldsMessages = pick(allMessages, fields2Validate)

    // TODO удалить после рефакторинга форм
    // после blur валидация срабатывает раньше, чем сетится модель, поэтому тут временный костылек
    yield delay(16)

    const model: Record<string, unknown> =
            yield select(dataSourceModelByPrefixSelector(id, prefix))
    const abortController = new AbortController()
    const pageId: string = yield select(dataSourcePageIdSelector(id))
    const pageUrl: string = yield select(makePageUrlByIdSelector(pageId))

    const currentProcess: AsyncValidation = {
        fields: fields2Validate,
        abortController,
        task: yield fork(validateModel, model, validation,
            {
                fields: fields2Validate,
                signal: abortController.signal,
                datasourceId: id,
                pageUrl,
            }),
    }

    asyncValidations[id] = currentProcess

    const messages: Awaited<ReturnType<typeof validateModel>> = yield currentProcess.task.toPromise()
    const fieldsToReset = fields2Validate.filter(field => isEmpty(messages[field]))

    if (!isEmpty(fieldsToReset)) {
        // @ts-ignore поправить типы
        yield put(resetValidation(id, fieldsToReset, prefix))
    }

    if (!isEmpty(messages) && !isEqual(messages, fieldsMessages)) {
        // @ts-ignore поправить типы
        yield put(failValidate(id, messages, prefix, meta))
    }

    asyncValidations[id] = null

    return !hasError(yield select(dataSourceErrors(id, prefix)))
}
