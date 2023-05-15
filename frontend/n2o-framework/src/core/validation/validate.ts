import { isEmpty } from 'lodash'

import {
    dataSourceModelByPrefixSelector,
    dataSourceValidationSelector,
} from '../../ducks/datasource/selectors'
import { failValidate, resetValidation } from '../../ducks/datasource/store'
import type { State as GlobalState } from '../../ducks/State'
import { ModelPrefix } from '../datasource/const'

import { hasError, validateModel } from './validateModel'
import { ValidationsKey } from './IValidation'

/**
 * Валидация datasource по стейту
 * @param {object} state
 * @param {string} datasourceId
 * @param {ModelPrefix} prefix
 * @param {Function} dispatch
 * @param {boolean} touched
 * @returns {boolean}
 * TODO переместить из ядра. Получается ядро завсит от редакса, а редакс от ядра
 */
export const validate = async (
    state: GlobalState,
    datasourceId: string,
    prefix = ModelPrefix.active,
    dispatch: (arg: unknown) => void = () => {},
    touched = false,
) => {
    const validation = dataSourceValidationSelector(
        datasourceId,
        prefix === ModelPrefix.filter ? ValidationsKey.FilterValidations : ValidationsKey.Validations,
    )(state)
    const model = dataSourceModelByPrefixSelector(datasourceId, prefix)(state)

    // @ts-ignore поправить типы
    dispatch(resetValidation(datasourceId, [], prefix))

    const messages = await validateModel(model, validation)

    if (!isEmpty(messages)) {
        // @ts-ignore поправить типы
        dispatch(failValidate(datasourceId, messages, prefix, { touched }))
    }

    return !hasError(messages)
}
