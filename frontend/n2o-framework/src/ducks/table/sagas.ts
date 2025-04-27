import { put, takeEvery, cancel, select } from 'redux-saga/effects'
import omit from 'lodash/omit'
import isEmpty from 'lodash/isEmpty'

import { registerWidget } from '../widgets/store'
import { Register } from '../widgets/Actions'
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

import { BodyCell, HeaderCell } from './Table'
import { registerTable } from './store'
import { getDefaultColumnState } from './constants'

function computeHeaderCell(widgetId: string, headerCell: HeaderCell, state: State) {
    const { children, id, disabled, visible } = headerCell

    let computedVisible = visible === undefined ? true : visible

    if (headerCell?.conditions?.visible) {
        computedVisible = resolveConditions(state, headerCell.conditions.visible).resolve
    }

    const processChildren = (children: HeaderCell[], parentId: string): HeaderCell[] => {
        return children.map((child) => {
            const { children: childChildren, id: childId, disabled: childDisabled, visible: childVisible } = child

            let childComputedVisible = childVisible === undefined ? true : childVisible

            if (child?.conditions?.visible) {
                childComputedVisible = resolveConditions(state, child.conditions.visible).resolve
            }

            const processedChildren = childChildren ? processChildren(childChildren, childId) : undefined

            return {
                ...getDefaultColumnState(),
                ...child,
                id: childId,
                columnId: childId,
                widgetId,
                parentId,
                visible: childComputedVisible,
                disabled: Boolean(childDisabled),
                children: processedChildren,
            }
        })
    }

    const resolvedChildren = children ? processChildren(children, id) : undefined

    return {
        ...getDefaultColumnState(),
        ...headerCell,
        id,
        columnId: id,
        widgetId,
        visible: computedVisible,
        disabled: Boolean(disabled),
        children: resolvedChildren,
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

    const computedHeaderCells = cells?.map((cell, index) => {
        // TODO временно
        // INFO backend не присылает id для мультиколонки
        if (!cell.id) {
            return computeHeaderCell(widgetId, { ...cell, id: `${widgetId}_${index}` }, state)
        }

        return computeHeaderCell(widgetId, cell, state)
    }) as HeaderCell[]

    const { cells: bodyCells } = body || {}

    // TODO временно
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

    // TODO временно, проблема в том что тут  еще нет datasource
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

export const sagas = [
    takeEvery(registerWidget, registerTableEffect),
]
