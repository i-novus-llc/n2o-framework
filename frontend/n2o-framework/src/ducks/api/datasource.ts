import { select, takeEvery } from 'redux-saga/effects'
import { createAction } from '@reduxjs/toolkit'
import isEmpty from 'lodash/isEmpty'

import { ModelPrefix } from '../../core/datasource/const'
import { Severity, Validation, ValidationResult } from '../../core/validation/types'
import { dataSourceValidationSelector } from '../datasource/selectors'
import { validate as validateSaga } from '../datasource/sagas/validate'
import { pickValidations } from '../form/helpers'
import { startValidate } from '../datasource/store'
import { hasError } from '../../core/validation/validateModel'
import { Form } from '../form/types'
import { makeFormsByModelLink } from '../form/selectors'
import { createRegexpWithContext } from '../../core/validation/utils'
import { N2OAction } from '../Action'
import { ContextType } from '../../core/datasource/ArrayField/Context'
import { ModelLink } from '../../core/models/types'

import { DATASOURCE_PREFIX } from './constants'
import { AsyncEffectWrapper } from './utils/effectWrapper'

export type Payload = {
    id: string
    model: ModelPrefix
    breakOn?: Severity.danger | Severity.warning | false
    fields?: string[]
    field?: `[${number}]`
}

export const validateCreator = createAction(
    `${DATASOURCE_PREFIX}validate`,
    (payload: Payload, meta: object) => ({
        payload,
        meta,
    }),
)

type ValidationMap = Record<string, Validation[]>

function* prepareFields(
    modelLink: ModelLink,
    list?: string[],
    evalContext?: Record<string, unknown>,
) {
    if (!list?.length) { return undefined }

    const validations: ValidationMap = yield select(dataSourceValidationSelector(modelLink.id, modelLink.prefix))

    if (isEmpty(validations)) { return undefined }

    const masks = list.map(field => createRegexpWithContext(
        field.replaceAll(/\[(\d+)]/g, '\\[$1]').replace('.*', '((\\.|\\[).+)?'),
        evalContext as ContextType,
    ))
    const forms: Form[] = yield select(makeFormsByModelLink(modelLink))
    const allFields = Object.keys(forms.map(form => form.fields).reduce((a, b) => ({ ...a, ...b }), {}))
    const fields = allFields.filter(field => masks.some(mask => mask.test(field)))

    const result: ValidationMap = {}

    fields.forEach((field) => {
        const validation = pickValidations(validations, field)

        if (validation?.size) { result[field] = [...validation] }
    })

    return result
}

export function* effect({ payload, meta }: N2OAction<string, Payload>) {
    const { id, model: prefix, breakOn, fields, field } = payload
    const index = (field && parseInt(field?.replace('[', ''), 10)) ?? undefined
    const modelLink = { id, prefix, index }
    const fields2Validate: undefined | ValidationMap = yield prepareFields(modelLink, fields, meta?.evalContext)

    const messages: Record<string, ValidationResult[]> = yield validateSaga(startValidate(
        modelLink,
        fields2Validate,
        { touched: true },
    ))

    if (breakOn && hasError(messages, breakOn)) {
        throw new Error('invalid model')
    }
}

export const sagas = [
    takeEvery(validateCreator.type, AsyncEffectWrapper(effect)),
]
