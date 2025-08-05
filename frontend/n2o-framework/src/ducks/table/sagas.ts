import { put, takeEvery, cancel, select } from 'redux-saga/effects'
import omit from 'lodash/omit'
import isEmpty from 'lodash/isEmpty'

import { registerWidget } from '../widgets/store'
import { type Register } from '../widgets/Actions'
import { State } from '../State'
import { resolveConditions } from '../../sagas/conditions'
import { getData } from '../../core/widget/useData'
import {
    type DatasourceSavedSettings,
    DATA_SOURCE_SAVED_SETTINGS,
    SAVED_SETTINGS,
} from '../../components/widgets/AdvancedTable/types'
import { createColumns } from '../../components/widgets/AdvancedTable/helpers'
import { updatePaging } from '../datasource/store'
import { dataSourceByIdSelector } from '../datasource/selectors'

import { type BodyCell, type HeaderCell } from './Table'
import { registerTable } from './store'
import { getDefaultColumnState } from './constants'

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

    if (!table) { yield cancel() }

    const state: State = yield select()

    const tableInitProps = { ...omit(initProps, 'table'), ...table }

    const { header, body } = tableInitProps
    const { cells } = header || {}
    const { cells: bodyCells } = body || {}

    const computedHeaderCells = cells?.map((cell, index) => {
        // INFO backend не присылает id для мультиколонки

        if (!cell.id) {
            return computeHeaderCell(widgetId, { ...cell, id: `${widgetId}_${index}` }, state)
        }

        return computeHeaderCell(widgetId, cell, state)
    }) as HeaderCell[]

    const computedBodyCells = bodyCells?.map((cell, index) => {
        // INFO backend не присылает id для колонок с тулбаром
        if (!cell.id) {
            return { ...cell, id: `${widgetId}_${index}` }
        }

        return cell
    }) as BodyCell[]

    const props = {
        ...tableInitProps,
        header: { cells: computedHeaderCells },
        body: { ...body, cells: computedBodyCells },
    }

    let savedProps = {}

    const savedSettings = getData(widgetId)

    // TODO временно, проблема в том что тут еще нет datasource
    const { paging = { page: 1, size: 5 }, sorting } = yield select(dataSourceByIdSelector(datasource))

    if (saveSettings &&
        !isEmpty(savedSettings) &&
        savedSettings[SAVED_SETTINGS.HEADER] &&
        savedSettings[SAVED_SETTINGS.BODY]
    ) {
        const savedHeaderCells = createColumns(computedHeaderCells, savedSettings[SAVED_SETTINGS.HEADER])
        const savedBodyCells = createColumns(computedBodyCells, savedSettings[SAVED_SETTINGS.BODY])

        const datasourceSettings = savedSettings[SAVED_SETTINGS.DATA_SOURCE_SETTINGS] as DatasourceSavedSettings

        if (!isEmpty(datasourceSettings)) {
            const paging = datasourceSettings[DATA_SOURCE_SAVED_SETTINGS.PAGING]

            if (paging) { yield put(updatePaging(datasource, paging)) }
        }

        savedProps = {
            ...props,
            header: { cells: savedHeaderCells },
            body: { ...body, cells: savedBodyCells },
        }
    }

    // TODO подумать как можно проще сохранить defaultProps и defaultDatasourceProps
    yield put(registerTable(
        widgetId,
        {
            ...props,
            ...savedProps,
            defaultProps: props,
            defaultDatasourceProps: { paging, sorting },
        },
    ))
}

export const sagas = [takeEvery(registerWidget, registerTableEffect)]
