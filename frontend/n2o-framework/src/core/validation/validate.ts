import { isEmpty } from 'lodash'
import { Dispatch } from 'redux'

import {
    dataSourceModelByPrefixSelector,
    dataSourceValidationSelector,
    dataSourcePageIdSelector,
} from '../../ducks/datasource/selectors'
import { makePageUrlByIdSelector } from '../../ducks/pages/selectors'
import { failValidate, resetValidation } from '../../ducks/datasource/store'
import type { State as GlobalState } from '../../ducks/State'
import { ModelPrefix } from '../datasource/const'

import { hasError, validateModel } from './validateModel'
import { ValidationsKey } from './types'

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
    const model = dataSourceModelByPrefixSelector(datasourceId, prefix)(state)

    dispatch(resetValidation(datasourceId, [], prefix))

    const pageId = dataSourcePageIdSelector(datasourceId)(state) || ''
    const pageUrl = makePageUrlByIdSelector(pageId)(state)

    const messages = await validateModel(model, validation, { datasourceId, pageUrl })

    if (!isEmpty(messages)) {
        dispatch(failValidate(datasourceId, messages, prefix, { touched }))
    }

    return !hasError(messages)
}
