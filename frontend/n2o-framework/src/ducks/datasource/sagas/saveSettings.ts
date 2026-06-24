import { select, put, fork, takeEvery } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'
import set from 'lodash/set'

import {
    type DataSourceState,
    type DataSourceCache,
    type DefaultDataSourceProps,
    DataSourceCacheKeys,
} from '../DataSource'
import { type DatasourceAction, type RegisterAction } from '../Actions'
import { type State } from '../../State'
import { dataSourceByIdSelector, dataSourceDefaultPropsSelector } from '../selectors'
import { register, dataRequest, setSorting, changeSize, changePage, resolveRequest } from '../store'
import { getData, setData, LOCAL_CONFIG_KEY } from '../../../core/widget/useData'
import { watchLocalStorage } from '../../../sagas/watchLocalStorage'

export const dataSourceSaveSettings: Record<string, {
    path: string
    transform(value: unknown, actionType?: string, fullState?: DataSourceState): unknown
}> = {
    [DataSourceCacheKeys.SORTING]: {
        path: 'sorting',
        // null сохранен чтобы сбросить сортировку, если она была локально отключена
        transform: value => (isEmpty(value) ? null : value),
    },
    [DataSourceCacheKeys.SIZE]: {
        path: 'paging.size',
        transform: value => value,
    },
    [DataSourceCacheKeys.PAGE]: {
        path: 'paging.page',
        // page сбрасывается до = 1 (при sorting и resize )
        transform: (value, actionType) => (
            (actionType === setSorting.type || actionType === changeSize.type) ? 1 : value),
    },
}

type Options = { datasource: string }

// Конфиг, если кэш пуст (локальные данные сброшены)
function buildConfigForEmptyCache(
    currentDataSourceProps: DataSourceState,
    defaultProps: DefaultDataSourceProps,
): DataSourceState {
    const { sorting: defaultSorting, paging: defaultPaging } = defaultProps
    const { page = 1, size = 1 } = defaultPaging || {}

    return {
        ...currentDataSourceProps,
        sorting: defaultSorting || currentDataSourceProps.sorting,
        paging: defaultPaging ? { ...currentDataSourceProps.paging, page, size } : currentDataSourceProps.paging,
    }
}
// Конфиг, если кэш содержит локальные обновления
function buildConfigForExistingCache(
    currentDataSourceProps: DataSourceState,
    cache: DataSourceCache,
): DataSourceState {
    const { paging, sorting } = cache

    return {
        ...currentDataSourceProps,
        sorting: sorting || {},
        paging: paging ? { ...currentDataSourceProps.paging, ...paging } : currentDataSourceProps.paging,
    }
}

// Эффект при изменении локальных данных datasource с другой вкладки
function* syncDataSourceFromStorage(cache: DataSourceCache, options: Options) {
    const { datasource } = options
    const state: State = yield select()

    const currentDataSourceProps = dataSourceByIdSelector(datasource)(state)
    const defaultProps: DefaultDataSourceProps = yield select(
        dataSourceDefaultPropsSelector(datasource),
    )

    const config = isEmpty(cache)
        ? buildConfigForEmptyCache(currentDataSourceProps, defaultProps)
        : buildConfigForExistingCache(currentDataSourceProps, cache)

    yield put(register(datasource, config))
}

// Обновляет локальные настройки TODO подумать над тем что нужно обновлять именно то что пришло, т.е. по типу экшена
function* updateDataSourceCache({ payload, type }: DatasourceAction) {
    const { id } = payload

    const dataSourceState: DataSourceState = yield select(dataSourceByIdSelector(id))
    const { saveSettings } = dataSourceState

    if (!saveSettings?.length) { return }

    const currentCache = getData<DataSourceCache>(id) || {}
    const nextCache = { ...currentCache }

    for (const key of saveSettings) {
        const mapping = dataSourceSaveSettings[key]

        let value = get(dataSourceState, mapping.path)

        value = mapping.transform(value, type, dataSourceState)

        set(nextCache, mapping.path, value)
    }

    setData(id, nextCache)
}

// Подписка эффекта в watchLocalStorage
function* subscribeToLocalCacheChanges(action: RegisterAction) {
    const { payload } = action
    const { initProps } = payload

    const { saveSettings } = initProps

    if (saveSettings) {
        const { id } = payload

        const key = `${LOCAL_CONFIG_KEY}_${id}`

        yield fork(watchLocalStorage<DataSourceCache, Options>, key, syncDataSourceFromStorage, { datasource: id })
    }
}

function* dataRequestWithCache(action: RegisterAction) {
    const { payload } = action
    const { initProps, id } = payload

    const { saveSettings } = initProps

    if (saveSettings?.length) {
        const currentCache = getData<DataSourceCache>(id) || {}

        if (currentCache) {
            yield put(dataRequest(id))
        }
    }
}

export const saveSettings = [
    takeEvery(register, subscribeToLocalCacheChanges),
    takeEvery(register, dataRequestWithCache),
    takeEvery([changeSize, setSorting, changePage, resolveRequest], updateDataSourceCache),
]
