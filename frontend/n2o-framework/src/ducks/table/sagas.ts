import { put, takeEvery, cancel, select } from 'redux-saga/effects'
import omit from 'lodash/omit'
import isEmpty from 'lodash/isEmpty'

import { registerWidget } from '../widgets/store'
import { type Register } from '../widgets/Actions'
import { type State } from '../State'
import { resolveConditions } from '../../sagas/conditions'
import { getData, setData } from '../../core/widget/useData'
import { TableStateCache } from '../../components/widgets/AdvancedTable/types'
import { mergeWithCache, prepareCellsToStore } from '../../components/widgets/AdvancedTable/helpers'

import { type Table, type HeaderCell } from './Table'
import { changeTableColumnParam, registerTable, switchTableParam, reorderColumn } from './store'
import { getDefaultColumnState } from './constants'
import { makeTableByIdSelector } from './selectors'
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

function* registerTableWidgetEffect(action: Register) {
    const { payload } = action
    const { widgetId, initProps } = payload
    const { table, saveSettings } = initProps

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

    if (saveSettings && !isEmpty(cachedSettings)) {
        const savedHeaderCells = mergeWithCache(computedHeaderCells, cachedSettings.header)
        const savedBodyCells = mergeWithCache(bodyCells, cachedSettings.body)

        cachedProps = {
            ...props,
            header: { cells: savedHeaderCells },
            body: { ...body, cells: savedBodyCells },
            textWrap: cachedSettings?.textWrap ?? props?.textWrap,
        }
    }

    yield put(registerTable(widgetId, { ...props, ...cachedProps, defaultProps: props }))
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
    takeEvery(registerWidget, registerTableWidgetEffect),
    takeEvery([changeTableColumnParam, switchTableParam, reorderColumn], onTableUpdateEffect),
]
