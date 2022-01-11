import {
    put,
    takeEvery,
    select,
    fork,
    cancel,
} from 'redux-saga/effects'

import { clearModel, copyModel, removeAllModel, removeModel, setModel } from '../models/store'
import { MODEL_PREFIX } from '../../core/datasource/const'

import { dataRequest as dataRequestSaga } from './sagas/dataRequest'
import { validate as validateSaga } from './sagas/validate'
import {
    changePage,
    changeSize,
    dataRequest,
    remove,
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

/** @type {Record<string, Array<string>>} Список активных задач dataRequest, которые надо отменить при дестрое */
const activeTasks = {}

// Очистка данных и отмена активных задач при дестрое ds
export function* removeSaga({ payload }) {
    const { id } = payload
    const tasks = activeTasks[id] || []

    for (const task of tasks) {
        yield cancel(task)
    }

    yield put(removeAllModel(id))
}

// Обёртка над dataRequestSaga для сохранения сылк на задачу, которую надо будет отменить в случае дестроя DS
export function* dataRequesWrapper(action) {
    const { datasource } = action.payload
    const task = yield fork(dataRequestSaga, action)

    activeTasks[datasource] = activeTasks[datasource] || []
    activeTasks[datasource].push(task)
    // фильтр завершенных задач, чтобы память не текла
    task.toPromise().finally(() => {
        activeTasks[datasource] = activeTasks[datasource].filter(activeTask => activeTask !== task)
    })
}

// Кеш предыдущего состояния для наблюдения за изменениями зависимостей
let prevState = {}

export default () => [
    takeEvery([setActiveModel, setFilter, setSourceModel, setMultiModel], resolveModelsSaga),
    takeEvery([setFilter, setSorting, changePage, changeSize], runDataRequest),
    takeEvery(dataRequest, dataRequesWrapper),
    takeEvery(startValidate, validateSaga),
    takeEvery(remove, removeSaga),
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
