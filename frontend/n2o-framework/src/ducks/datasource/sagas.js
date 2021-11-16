import {
    put,
    takeEvery,
} from 'redux-saga/effects'

import { setModel } from '../models/store'
import { MODEL_PREFIX } from '../../core/datasource/const'

import { dataRequest as dataRequestSaga } from './sagas/dataRequest'
import { validate as validateSaga } from './sagas/validate'
import {
    changePage,
    dataRequest,
    setActiveModel,
    setFilter,
    setMultiModel,
    setSorting,
    setSourceModel,
    startValidate,
} from './store'

// Мапинг изменения моделей
export function* resolveModelsSaga({ payload }) {
    const { id, model, prefix } = payload

    yield put(setModel(prefix, id, model))

    if (prefix === MODEL_PREFIX.filter) {
        yield put(dataRequest(id, { page: 1 }))
    }
}

// Запуск запроса за данными при изменении мета-данных (фильтр, сортировка, страница)
export function* runDataRequest({ payload }) {
    const { id, page } = payload

    yield put(dataRequest(id, { page: page || 1 }))
}

export default () => [
    takeEvery([setActiveModel, setFilter, setSourceModel, setMultiModel], resolveModelsSaga),
    takeEvery([setFilter, setSorting, changePage], runDataRequest),
    takeEvery(dataRequest, dataRequestSaga),
    takeEvery(startValidate, validateSaga),
]
