import React, { useCallback, useEffect, useRef, useState } from 'react'
import { defaultTo } from 'lodash'
import { useSelector } from 'react-redux'
import { createContext, useContext as useContextSelector } from 'use-context-selector'
import isEmpty from 'lodash/isEmpty'

import { N2OPagination } from '../Table/N2OPagination'
import WidgetLayout from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import {
    dataSourceModelByPrefixSelector,
} from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { Selection, TableActions, TableContainer } from '../../Table'
import { EMPTY_ARRAY } from '../../../utils/emptyTypes'
import { ToolbarOverlay } from '../../Table/provider/ToolbarOverlay'
import { useChangeFilter } from '../../Table/hooks/useChangeFilter'
import { useOnActionMethod } from '../hooks/useOnActionMethod'
import { getTableParam } from '../../../ducks/table/selectors'
import evalExpression from '../../../utils/evalExpression'
import { getValidationClass } from '../../../core/utils/getValidationClass'

import { useExpandAllRows } from './hooks/useExpandAllRows'
import { useTableActionReactions } from './hooks/useTableActionReactions'
import { VoidResolveColumnConditions } from './voidComponents/VoidResolveColumnConditions'
import { WithTableProps } from './WithTableProps'

const tableWidgetContext = createContext(null)

const EmptyComponent = () => (
    <div className="d-flex justify-content-center text-muted">Нет данных для отображения</div>
)

const defaultDataMapper = data => data

export const AdvancedTableContainer = ({
    id, disabled, toolbar, datasource, className, setPage, loading,
    fetchData, style, paging, table, size, count, validations,
    page, sorting, children, hasNext, isInit, setResolve,
    changeColumnParam, columnsState, tableConfig, switchTableParam,
    resolvedFilter, resolvedCells, paginationVisible,
    dataMapper = defaultDataMapper, components, setFilter,
}) => {
    const tableContainerElem = useRef(null)
    const [expandedRows, setExpandedRows] = useState([])
    const [filterErrors, setFilterErrors] = useState({})

    const textWrap = useSelector(getTableParam(id, 'textWrap'))
    const datasourceModel = useSelector((state) => {
        const model = (
            dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)(state) || EMPTY_ARRAY
        )

        return dataMapper(model)
    })
    const filterModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.filter))
    const selectedRows = useSelector((state) => {
        const models = dataSourceModelByPrefixSelector(datasource, ModelPrefix.selected)(state) || EMPTY_ARRAY

        if (models.length) { return models.map(({ id }) => id) }

        return EMPTY_ARRAY
    })
    const focusedRowValue = useSelector((state) => {
        const model = dataSourceModelByPrefixSelector(datasource, ModelPrefix.active)(state)

        return model ? model.id : null
    })

    const validateFilterField = useCallback((id, model, reset = false) => {
        const validation = validations[id]

        if (isEmpty(validation)) { return true }

        const isValid = validation.every(({ expression }) => evalExpression(expression, model))

        if (isValid || reset) {
            setFilterErrors({ ...filterErrors, [id]: null })

            return isValid
        }

        const [{ text, severity }] = validation
        const message = { text, severity }

        setFilterErrors({ ...filterErrors, [id]: { message, validationClass: getValidationClass(message) } })

        return isValid
    }, [filterErrors, validations])

    const { place = 'bottomLeft', showCount } = paging
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
                showCount={showCount}
                loading={loading}
            />
        ),
    }

    const { setActiveModel, setMultiModel, unsetMultiModel, updateDatasource } = useTableActionReactions(datasource)
    const onFilter = useChangeFilter(setFilter, datasource)
    const onRowClickAction = useOnActionMethod(datasource, tableConfig?.body?.row?.click)
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

            case TableActions.onChangeFilter: {
                onFilter(payload.model)

                break
            }

            case TableActions.onUpdateModel: {
                updateDatasource(payload.model, payload.rowIndex)

                break
            }

            default: { break }
        }
    }, [setMultiModel, unsetMultiModel, setActiveModel, onRowClickAction, onFilter, updateDatasource])
    const onClickToolbarActionButton = useCallback((model) => { setActiveModel(model) }, [setActiveModel])
    const isNeedSetResolveModel = table.rowSelection !== Selection.None && defaultTo(table.autoSelect, true)

    useEffect(() => {
        if (isNeedSetResolveModel && datasourceModel) {
            setResolve(datasourceModel[0])
        }
    }, [datasourceModel, setResolve, isNeedSetResolveModel])

    useExpandAllRows(setExpandedRows, children, datasourceModel)

    return (
        <tableWidgetContext.Provider value={{ changeColumnParam, columnsState, switchTableParam }}>
            <VoidResolveColumnConditions
                columnsState={columnsState}
                changeColumnParam={changeColumnParam}
                changeTableParam={switchTableParam}
                widgetId={id}
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
                    pagination={pagination}
                    showCount={showCount}
                >
                    {isInit ? (
                        <TableContainer
                            filterValue={filterModel}
                            refContainerElem={tableContainerElem}
                            actionListener={actionListener}
                            childrenToggleState={children}
                            sorting={sorting}
                            data={datasourceModel}
                            cells={resolvedCells}
                            tableConfig={tableConfig}
                            isTextWrap={textWrap}
                            selectedRows={selectedRows}
                            expandedRows={expandedRows}
                            focusedRowValue={focusedRowValue}
                            EmptyContent={<EmptyComponent />}
                            validateFilterField={validateFilterField}
                            filterErrors={filterErrors}
                            components={components}
                        />
                    ) : null}
                </WidgetLayout>
            </ToolbarOverlay>
        </tableWidgetContext.Provider>
    )
}

AdvancedTableContainer.displayName = 'AdvancedTableContainer'

export const AdvancedTableWidget = WidgetHOC(WithTableProps(AdvancedTableContainer))

AdvancedTableWidget.displayName = 'AdvancedTableWidget'

export const useTableWidget = () => {
    const context = useContextSelector(tableWidgetContext)

    if (!context) {
        throw new Error('useTableWidget must be used with tableWidgetContext')
    }

    return context
}
