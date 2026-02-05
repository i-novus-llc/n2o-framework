import { Dispatch } from 'redux'

import {
    dataSourceModelByPrefixSelector,
    dataSourceValidationSelector,
    dataSourcePageIdSelector,
} from '../../ducks/datasource/selectors'
import { makePageUrlByIdSelector } from '../../ducks/pages/selectors'
import { endValidation } from '../../ducks/datasource/store'
import type { State as GlobalState } from '../../ducks/State'
import { ModelPrefix } from '../datasource/const'

import { hasError, validateModel } from './validateModel'
import { ValidationsKey } from './types'
import { addFieldMessages } from './addFieldMessages'

/**
 * Валидация datasource по стейту
 * @param {object} state
 * @param {string} datasourceId
 * @param {ModelPrefix} prefix
 * @param {Function} dispatch
 * @param {boolean} touched
 * @returns {boolean}
 * TODO переместить из ядра. Получается ядро зависит от редакса, а редакс от ядра
 */

export const validate = async (
    state: GlobalState,
    datasourceId: string,
    prefix = ModelPrefix.active,
    dispatch: Dispatch | ((arg: unknown) => void) = () => {},
    touched = false,
) => {
    const validation = dataSourceValidationSelector(
        datasourceId,
        prefix === ModelPrefix.filter ? ValidationsKey.FilterValidations : ValidationsKey.Validations,
    )(state)
    const model = dataSourceModelByPrefixSelector(datasourceId, prefix)(state) as Record<string, unknown>

    const pageId = dataSourcePageIdSelector(datasourceId)(state) || ''
    const pageUrl = makePageUrlByIdSelector(pageId)(state)

    const modelMessages = await validateModel(model, validation, { datasourceId, pageUrl })
    const messages = addFieldMessages(datasourceId, modelMessages, state)

    dispatch(endValidation({ id: datasourceId, messages, prefix }, { touched }))

    return !hasError(messages)
}
