import React, { useCallback, useContext, useEffect, useMemo, useRef, useState } from 'react'
import { defaultTo } from 'lodash'
import { useSelector } from 'react-redux'
import omit from 'lodash/omit'
import { compose } from 'recompose'
import { createContext, useContext as useContextSelector } from 'use-context-selector'

import { N2OPagination } from '../Table/N2OPagination'
import WidgetLayout from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { Selection, TableActions, TableContainer } from '../../Table'
import { useCheckAccess } from '../../../core/auth/SecurityController'
import { withSecurityList } from '../../../core/auth/withSecurity'
import { EMPTY_ARRAY } from '../../../utils/emptyTypes'
import { ToolbarOverlay } from '../../Table/provider/ToolbarOverlay'
import { useOnActionMethod } from '../hooks/useOnActionMethod'

import { useExpandAllRows } from './hooks/useExpandAllRows'
import { useResolveCellsVisible } from './hooks/useResolveCellsVisible'
import { useColumnsState } from './hooks/useColumnsState'
import { useTableActionReactions } from './hooks/useTableActionReactions'
import { VoidResolveColumnConditions } from './voidComponents/VoidResolveColumnConditions'

const tableWidgetContext = createContext(null)

const EmptyComponent = () => (
    <div className="d-flex justify-content-center text-muted">Нет данных для отображения</div>
)

const AdvancedTableContainer = (props) => {
    const {
        id, disabled, toolbar, datasource, className, setPage, loading, fetchData,
        style, paging, filter, table, size, count, page, sorting, children, hasNext, isInit,
        setResolve,
    } = props
    const { resolveProps } = useContext(FactoryContext)

    const tableContainerElem = useRef(null)
    const [expandedRows, setExpandedRows] = useState([])

    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source))
    const selectedRows = useSelector((state) => {
        const models = dataSourceModelByPrefixSelector(datasource, ModelPrefix.selected)(state) || EMPTY_ARRAY

        if (models.length) {
            return models.map(({ id }) => id)
        }

        return EMPTY_ARRAY
    })
    const focusedRowValue = useSelector((state) => {
        const model = dataSourceModelByPrefixSelector(datasource, ModelPrefix.active)(state)

        return model ? model.id : null
    })

    const cells = useMemo(() => ({
        header: table.header.cells.map(cell => resolveProps(cell)),
        body: table.body.cells.map(cell => resolveProps(cell)),
    }), [table.header.cells, table.body.cells, resolveProps])
    const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps])

    const [columnsState, changeColumnParam] = useColumnsState(cells.header)
    const resolvedCells = useResolveCellsVisible(cells, columnsState)

    const tableConfig = useMemo(() => {
        const config = omit(table, ['autoSelect', 'autoFocus', 'textWrap', 'header.cells', 'body.cells'])

        return resolveProps(config)
    }, [resolveProps, table])

    const paginationVisible = useMemo(() => columnsState.some(column => column.visible), [columnsState])
    const hasSecurityAccess = useCheckAccess(tableConfig.body?.row?.security)

    const { place = 'bottomLeft' } = paging
    const pagination = {
        [place]: (
            <N2OPagination
                {...paging}
                hasNext={hasNext}
                size={size}
                count={count}
                activePage={page}
                datasource={datasourceModel}
                setPage={setPage}
                visible={paginationVisible}
            />
        ),
    }

    const { setActiveModel, setMultiModel, unsetMultiModel } = useTableActionReactions(datasource)
    const onRowClickAction = useOnActionMethod(id, tableConfig?.body?.row?.click)
    const actionListener = useCallback((action, payload) => {
        switch (action) {
            case TableActions.toggleExpandRow: {
                const { rowValue, isOpen } = payload

                if (isOpen) {
                    setExpandedRows(state => [...state, rowValue])
                } else {
                    setExpandedRows(state => state.filter(expandedRow => expandedRow !== rowValue))
                }

                break
            }

            case TableActions.selectRows: {
                setMultiModel(payload.listRowValue)

                break
            }

            case TableActions.selectSingleRow: {
                setMultiModel(payload.rowValue)

                break
            }

            case TableActions.deselectRows: {
                unsetMultiModel(payload.listRowValue)

                break
            }

            case TableActions.setFocusOnRow: {
                setActiveModel(payload.model)

                break
            }

            case TableActions.onRowClick: {
                onRowClickAction(payload.model)

                break
            }

            default: {
                break
            }
        }
    }, [onRowClickAction, setActiveModel, setMultiModel, unsetMultiModel])
    const onClickToolbarActionButton = useCallback((model) => {
        setActiveModel(model)
    }, [setActiveModel])
    const isNeedSetResolveModel = table.rowSelection !== Selection.None && defaultTo(table.autoSelect, true)

    useEffect(() => {
        if (isNeedSetResolveModel && datasourceModel) {
            setResolve(datasourceModel[0])
        }
    }, [datasourceModel, setResolve, isNeedSetResolveModel])

    useExpandAllRows(setExpandedRows, children, datasourceModel)

    return (
        <tableWidgetContext.Provider value={{ changeColumnParam, columnsState }}>
            <VoidResolveColumnConditions
                columnsState={columnsState}
                changeColumnParam={changeColumnParam}
            />
            <ToolbarOverlay
                onClickActionButton={onClickToolbarActionButton}
                refContainerElem={tableContainerElem}
                overlay={tableConfig.body?.row?.overlay}
            >
                <WidgetLayout
                    disabled={disabled}
                    widgetId={id}
                    datasource={datasource}
                    toolbar={toolbar}
                    filter={resolvedFilter}
                    className={className}
                    style={style}
                    fetchData={fetchData}
                    loading={loading}
                    {...pagination}
                >
                    {isInit ? (
                        <TableContainer
                            refContainerElem={tableContainerElem}
                            actionListener={actionListener}
                            hasSecurityAccess={hasSecurityAccess}
                            childrenToggleState={children}
                            sorting={sorting}
                            data={datasourceModel}
                            cells={resolvedCells}
                            tableConfig={tableConfig}
                            id={id}
                            isTextWrap={table.textWrap}
                            selectedRows={selectedRows}
                            expandedRows={expandedRows}
                            focusedRowValue={focusedRowValue}
                            EmptyContent={<EmptyComponent />}
                        />
                    ) : null}
                </WidgetLayout>
            </ToolbarOverlay>
        </tableWidgetContext.Provider>
    )
}

AdvancedTableContainer.displayName = 'AdvancedTableWidget'

export const AdvancedTableWidget = compose(
    WidgetHOC,
)(withSecurityList(AdvancedTableContainer, 'table.header.cells'))

export const useTableWidget = () => {
    const context = useContextSelector(tableWidgetContext)

    if (!context) {
        throw new Error('useTableWidget must be used with tableWidgetContext')
    }

    return context
}
