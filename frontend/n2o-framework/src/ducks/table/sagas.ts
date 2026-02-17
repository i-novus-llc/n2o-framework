import { put, takeEvery, cancel, select, delay, fork } from 'redux-saga/effects'
import omit from 'lodash/omit'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'

import { registerWidget } from '../widgets/store'
import { type Register } from '../widgets/Actions'
import { type State } from '../State'
import { DataSource, DataSourceState, DefaultDataSourceProps } from '../datasource/DataSource'
import { resolveConditions } from '../../sagas/conditions'
import { getData, LOCAL_WIDGET_CONFIG_KEY, setData } from '../../core/widget/useData'
import { TableStateCache } from '../../components/widgets/AdvancedTable/types'
import { mergeWithCache, prepareCellsToStore } from '../../components/widgets/AdvancedTable/helpers'
import { changePage, changeSize, dataRequest, register, setSorting, updatePaging } from '../datasource/store'
import { dataSourceByIdSelector, dataSourceDefaultPropsSelector } from '../datasource/selectors'
import { type DatasourceAction } from '../datasource/Actions'
import { watchLocalStorage } from '../../sagas/watchLocalStorage'

import { type Table, type HeaderCell } from './Table'
import { changeTableColumnParam, registerTable, switchTableParam, reorderColumn } from './store'
import { getDefaultColumnState } from './constants'
import { makeTableByDatasourceSelector, makeTableByIdSelector } from './selectors'
import { ChangeTableColumnParam, RegisterTable, SwitchTableColumnParam } from './Actions'

export interface Options {
    datasource: string
}

// TODO сломается при фильтрации, т.к. при фильтре сбрасывается page, но сами фильтры не хранятся в локальных настройках

// @INFO эффект при изменении локальных данных ds таблицы с другой вкладки
function* dataSourceStorageEvent(cache: TableStateCache, options: Options) {
    const { datasource } = options
    const state: State = yield select()

    const reduxDataSourceProps = dataSourceByIdSelector(datasource)(state)

    // @INFO локальные данные были сброшены
    if (isEmpty(cache)) {
        const defaultDataSourceProps: DefaultDataSourceProps = yield select(dataSourceDefaultPropsSelector(datasource))

        const { sorting: defaultSorting, paging: defaultPaging } = defaultDataSourceProps

        const newProps = {
            ...reduxDataSourceProps,
            // TODO Проверки из за типизации DefaultDataSourceProps, все параметры не обязательны
            sorting: defaultSorting || reduxDataSourceProps.sorting,
            paging: defaultPaging ? { ...reduxDataSourceProps.paging, ...defaultPaging } : reduxDataSourceProps.paging,
        }

        yield put(register(datasource, newProps))

        return
    }

    // @INFO локальные данные были обновлены
    const { datasourceFeatures } = cache

    const { paging, sorting } = datasourceFeatures

    const newProps = {
        ...reduxDataSourceProps,
        // TODO Проверки из за типизации DefaultDataSourceProps, все параметры не обязательны
        sorting: sorting || {},
        paging: paging ? { ...reduxDataSourceProps.paging, ...paging } : reduxDataSourceProps.paging,
    }

    yield put(register(datasource, newProps))
    yield put(dataRequest(datasource, {
        page: newProps.paging.page,
        size: newProps.paging.size,
    }))
}

function computeHeaderCell(
    widgetId: string,
    headerCell: HeaderCell,
    state: State,
) {
    const computeVisibility = (cell: HeaderCell): boolean => {
        if (cell?.conditions?.visible) {
            return resolveConditions(state, cell.conditions.visible).resolve
        }

        return cell.visible ?? true
    }

    const processChildren = (children: HeaderCell[], parentId: string): HeaderCell[] => {
        return children.map(child => ({
            ...getDefaultColumnState(),
            ...child,
            id: child.id,
            columnId: child.id,
            widgetId,
            parentId,
            enabled: child.enabled,
            children: child.children ? processChildren(child.children, child.id) : undefined,
            visible: computeVisibility(child),
            visibleState: child.children ? true : child.visibleState,
        }))
    }

    const computedVisible = computeVisibility(headerCell)
    const resolvedChildren = headerCell.children ? processChildren(headerCell.children, headerCell.id) : undefined

    return {
        ...getDefaultColumnState(),
        ...headerCell,
        id: headerCell.id,
        columnId: headerCell.id,
        widgetId,
        enabled: headerCell.enabled,
        children: resolvedChildren,
        visible: computedVisible,
        visibleState: headerCell.children ? true : headerCell.visibleState,
    }
}

function* registerTableWidgetEffect(action: Register) {
    const { payload } = action
    const { widgetId, initProps } = payload
    const { table, datasource, saveSettings } = initProps

    if (!table) { yield cancel() }

    if (saveSettings) {
        const key = `${LOCAL_WIDGET_CONFIG_KEY}_${payload.widgetId}`

        yield fork(
            watchLocalStorage<TableStateCache, Options>,
            key,
            dataSourceStorageEvent,
            { datasource },
        )
    }

    /** Необходимо дождаться регистрации datasource */
    yield delay(50)

    const state: State = yield select()

    const tableInitProps = { ...omit(initProps, 'table'), ...table }

    const { header, body } = tableInitProps
    const { cells } = header || {}
    const { cells: bodyCells } = body || {}

    const computedHeaderCells = cells?.map(cell => computeHeaderCell(widgetId, cell, state)) as HeaderCell[]

    const props = {
        ...tableInitProps,
        header: { cells: computedHeaderCells },
        body: { ...body, cells: bodyCells },
    }

    let cachedProps = {}

    const cachedSettings = getData<TableStateCache>(widgetId)

    if (saveSettings && !isEmpty(cachedSettings)) {
        const savedHeaderCells = mergeWithCache(computedHeaderCells, cachedSettings.header)
        const savedBodyCells = mergeWithCache(bodyCells, cachedSettings.body)

        const { paging } = cachedSettings.datasourceFeatures || {}

        if (paging) { yield put(updatePaging(datasource, paging)) }

        cachedProps = {
            ...props,
            header: { cells: savedHeaderCells },
            body: { ...body, cells: savedBodyCells },
            textWrap: cachedSettings?.textWrap ?? props?.textWrap,
        }
    }

    yield put(registerTable(widgetId, { ...props, ...cachedProps, defaultProps: props }))
}

// Обновляет локальные настройки
function* onDatasourceUpdateEffect({ payload, type }: DatasourceAction) {
    const { id: datasourceId } = payload

    yield delay(16)

    const { sorting, paging }: DataSourceState = yield select(dataSourceByIdSelector(datasourceId))
    const { saveSettings, id }: Table = yield select(makeTableByDatasourceSelector(datasourceId))

    if (saveSettings) {
        const cache = getData<TableStateCache>(id)

        const next = {
            ...cache,
            // @INFO настройки таблицы для datasource
            datasourceFeatures: {
                sorting: isEmpty(sorting) ? undefined : sorting,
                // @INFO т.к. ручная сортировка сбрасывает page на 1
                paging: type === setSorting.type ? { ...paging, page: 1 } : paging,
            },
        }

        setData(id, next)
    }
}

function* onTableUpdateEffect({ payload }: ChangeTableColumnParam | SwitchTableColumnParam) {
    const { widgetId } = payload
    const { saveSettings, textWrap, body, header }: Table = yield select(makeTableByIdSelector(widgetId))

    if (saveSettings) {
        const cache = getData<TableStateCache>(widgetId)

        setData(widgetId, {
            ...cache,
            textWrap,
            header: prepareCellsToStore(header.cells),
            body: prepareCellsToStore(body.cells),
        })
    }
}

function* tableDataRequest(
    datasource: string,
    sorting: TableStateCache['datasourceFeatures']['sorting'] | DefaultDataSourceProps['sorting'] = {},
    paging: TableStateCache['datasourceFeatures']['paging'] | DefaultDataSourceProps['paging'] = {},
) {
    const state: State = yield select()

    const reduxDataSourceProps = dataSourceByIdSelector(datasource)(state)
    const { sorting: reduxSorting } = reduxDataSourceProps

    if (!isEqual(sorting, reduxSorting)) {
        // @INFO пока костыль, при перерегистрации таблицы (прим. удалены локальные данные), пока никак не обновить сортировку, кроме перерегистрации
        yield put(register(datasource, { ...reduxDataSourceProps, sorting }))
    }

    const computedPaging = { ...DataSource.defaultState.paging, ...paging }

    yield put(dataRequest(datasource, { page: computedPaging.page, size: computedPaging.size }))
}

// Запрос за данными в зависимости от наличия локальных настроек
function* onRegisterTableEffect(action: RegisterTable) {
    const { payload } = action
    const { initProps } = payload
    const { id, saveSettings } = initProps
    const datasource = initProps.datasource as string

    if (saveSettings) {
        const cache = getData<TableStateCache>(id)

        // Есть локальные настройки, запрос с datasourceFeatures
        if (cache?.datasourceFeatures) {
            const { datasourceFeatures } = cache
            const { sorting = {}, paging = {} } = datasourceFeatures

            yield* tableDataRequest(datasource, sorting, paging)

            return
        }

        // Нет локальных настроек, запрос с defaultDataSourceProps
        const defaultDataSourceProps: DefaultDataSourceProps = yield select(dataSourceDefaultPropsSelector(datasource))
        const { sorting = {}, paging = {} } = defaultDataSourceProps

        yield* tableDataRequest(datasource, sorting, paging)
    }
}

export const sagas = [
    takeEvery(registerWidget, registerTableWidgetEffect),
    takeEvery(registerTable, onRegisterTableEffect),
    takeEvery([changeSize, setSorting, changePage], onDatasourceUpdateEffect),
    takeEvery([changeTableColumnParam, switchTableParam, reorderColumn], onTableUpdateEffect),
]
