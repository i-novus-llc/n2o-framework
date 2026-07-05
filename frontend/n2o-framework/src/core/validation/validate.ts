import { Dispatch } from 'redux'

import {
    dataSourceValidationSelector,
    dataSourcePageIdSelector,
} from '../../ducks/datasource/selectors'
import { makePageUrlByIdSelector } from '../../ducks/pages/selectors'
import { getModelSelector } from '../../ducks/models/selectors'
import { endValidation } from '../../ducks/datasource/store'
import type { State as GlobalState } from '../../ducks/State'
import { Model, ModelLink, ModelPrefix } from '../models/types'

import { hasError, validateModel } from './validateModel'
import { addFieldMessages } from './addFieldMessages'
import { ExtraValidationConfig, Validation, ValidationResult } from './types'

async function validateSingle(
    state: GlobalState,
    model: Model,
    modelLink: ModelLink,
    validation: Record<string, Validation[]>,
    options: ExtraValidationConfig,
    dispatch: Dispatch | ((arg: unknown) => void),
) {
    const modelMessages = await validateModel(model, validation, options)
    const messages = addFieldMessages(modelLink, modelMessages, state)

    dispatch(endValidation({ modelLink, messages: modelMessages }, { touched: true }))

    return messages
}

async function validateMulti(
    state: GlobalState,
    model: Model[],
    modelLink: ModelLink,
    validation: Record<string, Validation[]>,
    options: ExtraValidationConfig,
    dispatch: Dispatch | ((arg: unknown) => void),
) {
    let messages: Record<string, ValidationResult[]> = {}

    for (let i = 0; i < model.length; i++) {
        const modelMessages = await validateSingle(
            state,
            model[i],
            { ...modelLink, index: i },
            validation,
            options,
            dispatch,
        )

        messages = {
            ...messages,
            ...Object.fromEntries(Object.entries(modelMessages).map(([key, value]) => [`[${i}]${key}`, value])),
        }
    }

    return messages
}

/**
 * Валидация datasource по стейту
 * !!! Дублирует функционал модуля ducks\datasource\sagas\validate но без использования redux-saga
 * !!! Все изменения должны быть зеркальными до удаления зашитого в кнопки вызова валидации и текущего файла
 */
export const validate = async <Prefix extends ModelPrefix>(
    state: GlobalState,
    modelLink: ModelLink<Prefix>,
    dispatch: Dispatch | ((arg: unknown) => void) = () => {},
) => {
    const { id, prefix, index } = modelLink
    const validation = dataSourceValidationSelector(id, prefix)(state) || {}
    const isMulti = prefix === ModelPrefix.selected || (prefix === ModelPrefix.source && typeof index === 'undefined')
    const model = getModelSelector(modelLink)(state)

    const pageId = dataSourcePageIdSelector(id)(state) || ''
    const pageUrl = makePageUrlByIdSelector(pageId)(state)
    const validateMethod = isMulti ? validateMulti : validateSingle

    const messages = await validateMethod(
        state,
        // @ts-ignore тип model не подхватывается автоматом из isMulti
        model,
        modelLink,
        validation,
        { datasourceId: id, pageUrl },
        dispatch,
    )

    return !hasError(messages)
}
