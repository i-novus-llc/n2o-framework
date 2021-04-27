import { call, put, take, cancelled } from 'redux-saga/effects'
import get from 'lodash/get'
import values from 'lodash/values'
import keys from 'lodash/keys'
import first from 'lodash/first'
import includes from 'lodash/includes'
import split from 'lodash/split'
import isEmpty from 'lodash/isEmpty'

import {
    fetchStart,
    fetchEnd,
    fetchCancel,
    fetchError,
} from '../actions/fetch'
import defaultApiProvider from '../core/api'
import { FETCH_ERROR_CONTINUE } from '../constants/fetch'
import {
    isValidRangeModel,
    isRequiredRangeModel,
    modelHasRange,
} from '../utils/checkRangeModel'

export default function* fetchSaga(
    fetchType,
    options,
    apiProvider = defaultApiProvider,
    action,
    state,
) {
    const model = get(options, 'model')
    const modelId = get(model, 'id')
    const modelValues = values(model)

    const widgetId = get(action, 'payload.widgetId')
    const widgetValidation = get(state, `widgets.${widgetId}.validation`)
    const hasWidgetValidation = !isEmpty(widgetValidation)
    const validationKey = first(keys(widgetValidation))
    const treePath = includes(split(validationKey, ''), '.')
    const registeredFields = get(state, `form.${widgetId}.registeredFields`)
    const isValidForm =
    get(registeredFields, [validationKey, 'message']) === null
    const isVisibleForm =
    get(registeredFields, [validationKey, 'visible']) === true
    const treePathFields = registeredFields && treePath

    const isRangeModel =
    modelHasRange(modelValues) && isRequiredRangeModel(modelValues, modelId)
    const isValidRange = isValidRangeModel(modelValues)

    try {
        if (
            (isRangeModel && !isValidRange && hasWidgetValidation) ||
      (treePathFields && !isValidForm && isVisibleForm)
        ) {
            return
        }
        yield put(fetchStart(fetchType, options))
        const response = yield call(apiProvider, fetchType, options)

        yield put(fetchEnd(fetchType, options, response))

        return response
    } catch (error) {
        yield put(fetchError(fetchType, options, error))
        yield take(FETCH_ERROR_CONTINUE)
        throw error
    } finally {
        if (yield cancelled()) {
            yield put(fetchCancel(fetchType, options))

            return
        }
    }
}
