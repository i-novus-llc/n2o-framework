import { isEmpty } from 'lodash'

import { dataSourceModelsSelector, dataSourceValidationSelector } from '../../ducks/datasource/selectors'
import { failValidate } from '../../ducks/datasource/store.js'
import { State as GlobalState } from '../../ducks/State'

import { ModelPrefix } from '../datasource/const'

import { hasError, validateModel } from './validateModel'

/**
 * Валидация datasource по стейту
 * @param {object} state
 * @param {string} datasourceId
 * @param {Function} dispatch
 * @returns {boolean}
 * TODO переместить из ядра. Получается ядро завсит от редакса, а редакс от ядра
 */
 export const validate = async (
    state: GlobalState,
    datasourceId: string,
    dispatch: Function,
    touched: boolean,
) => {
    const validation = dataSourceValidationSelector(datasourceId)(state)
    const models = dataSourceModelsSelector(datasourceId)(state)
    const model = models[ModelPrefix.active] || {}

    const messages = await validateModel(model, validation)

    if (!isEmpty(messages)) {
        dispatch(failValidate(datasourceId, messages, ModelPrefix.active, { touched }))
    }

    return !hasError(messages)
}
