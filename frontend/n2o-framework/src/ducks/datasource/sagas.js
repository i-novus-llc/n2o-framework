import {
    put,
    takeEvery,
    select,
} from 'redux-saga/effects'

import { clearModel, copyModel, removeAllModel, removeModel, setModel } from '../models/store'
import { MODEL_PREFIX } from '../../core/datasource/const'

import { dataRequest as dataRequestSaga } from './sagas/dataRequest'
import { validate as validateSaga } from './sagas/validate'
import {
    changePage,
    changeSize,
    dataRequest,
    setActiveModel,
    setFilter,
    setMultiModel,
    setSorting,
    setSourceModel,
    startValidate,
} from './store'
import { watchDependencies } from './sagas/dependencies'

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

// Кеш предыдущего состояния для наблюдения за изменениями зависимостей
let prevState = {}

export default () => [
    takeEvery([setActiveModel, setFilter, setSourceModel, setMultiModel], resolveModelsSaga),
    takeEvery([setFilter, setSorting, changePage, changeSize], runDataRequest),
    takeEvery(dataRequest, dataRequestSaga),
    takeEvery(startValidate, validateSaga),
    takeEvery([setModel, removeModel, removeAllModel, copyModel, clearModel], function* watcher(action) {
        yield watchDependencies(action, prevState)
        prevState = yield select()
    }),
    takeEvery(action => action.meta?.refresh?.datasources, function* refrashSage({ meta }) {
        const { refresh } = meta
        const { datasources } = refresh

        for (const datasource of datasources) {
            yield put(dataRequest(datasource))
        }
    }),
]
