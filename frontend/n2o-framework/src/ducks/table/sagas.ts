import { put, takeEvery, cancel, select, delay } from 'redux-saga/effects'
import omit from 'lodash/omit'
import isEmpty from 'lodash/isEmpty'

import { registerWidget } from '../widgets/store'
import { type Register } from '../widgets/Actions'
import { State } from '../State'
import { resolveConditions } from '../../sagas/conditions'
import { getData, setData } from '../../core/widget/useData'
import { TableStateCache } from '../../components/widgets/AdvancedTable/types'
import { mergeWithCache, prepareCellsToStore } from '../../components/widgets/AdvancedTable/helpers'
import { changePage, changeSize, setSorting, updatePaging } from '../datasource/store'
import { dataSourceByIdSelector } from '../datasource/selectors'
import { makePageMetadataByIdSelector } from '../pages/selectors'
import { DataSourceState } from '../datasource/DataSource'
import { DatasourceAction } from '../datasource/Actions'

import { Table, type HeaderCell } from './Table'
import { changeTableColumnParam, registerTable, switchTableParam } from './store'
import { getDefaultColumnState } from './constants'
import { makeTableByDatasourceSelector, makeTableByIdSelector } from './selectors'
import { ChangeTableColumnParam, SwitchTableColumnParam } from './Actions'

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

function* registerTableEffect(action: Register) {
    const { payload } = action
    const { widgetId, initProps } = payload
    const { table, datasource, saveSettings } = initProps

    /** Необходимо дождаться регистрации datasource */
    yield delay(50)

    if (!table) { yield cancel() }

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

    const { sorting, pageId, id } = yield select(dataSourceByIdSelector(datasource))
    // @ts-ignore FIXME временно
    const metaData = yield select(makePageMetadataByIdSelector(pageId))

    const { datasources = {} } = metaData || {}
    const pageDs = datasources[id] || {}
    const { paging } = pageDs

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

    // TODO подумать как можно проще сохранить defaultProps и defaultDatasourceProps
    yield put(registerTable(
        widgetId,
        {
            ...props,
            ...cachedProps,
            defaultProps: props,
            defaultDatasourceProps: { paging, sorting },
        },
    ))
}

function* onDatasourceUpdateEffect({ payload }: DatasourceAction) {
    const { id: datasourceId } = payload
    const { sorting, paging }: DataSourceState = yield select(dataSourceByIdSelector(datasourceId))
    const { saveSettings, id }: Table = yield select(makeTableByDatasourceSelector(datasourceId))

    if (saveSettings) {
        const cache = getData<TableStateCache>(id)
        const next = {
            ...cache,
            datasourceFeatures: {
                sorting: isEmpty(sorting) ? undefined : sorting,
                paging,
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

export const sagas = [
    takeEvery(registerWidget, registerTableEffect),
    takeEvery([changeSize, setSorting, changePage], onDatasourceUpdateEffect),
    takeEvery([changeTableColumnParam, switchTableParam], onTableUpdateEffect),
]
