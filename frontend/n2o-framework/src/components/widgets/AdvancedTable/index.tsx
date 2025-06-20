import React, { useCallback, useEffect, useRef, useState } from 'react'
import defaultTo from 'lodash/defaultTo'
import { useSelector } from 'react-redux'
import { createContext, useContext as useContextSelector } from 'use-context-selector'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import { N2OPagination } from '../Table/N2OPagination'
import StandardWidget from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { ChildrenToggleState, Selection, TableActions, TableContainer } from '../../Table'
import { EMPTY_ARRAY, NOOP_FUNCTION } from '../../../utils/emptyTypes'
import { ToolbarOverlay } from '../../Table/provider/ToolbarOverlay'
import { useChangeFilter } from '../../Table/hooks/useChangeFilter'
import { useOnActionMethod } from '../hooks/useOnActionMethod'
import { evalExpression } from '../../../utils/evalExpression'
import { getValidationClass } from '../../../core/utils/getValidationClass'
import { type State } from '../../../ducks/State'
import { type Data, type SelectedRows } from '../../Table/types/general'

import { useExpandAllRows } from './hooks/useExpandAllRows'
import { useTableActionReactions } from './hooks/useTableActionReactions'
import { VoidResolveColumnConditions } from './voidComponents/VoidResolveColumnConditions'
import { WithTableProps } from './WithTableProps'
import { type AdvancedTableWidgetProps } from './types'
import { TableSettingsObserver } from './TableSettingsObserver'

const tableWidgetContext = createContext(null as unknown)

type tableWidgetContextType = Pick<AdvancedTableWidgetProps, 'changeColumnParam' | 'switchTableParam' | 'resetSettings'>

const EmptyComponent = () => (
    <div className="d-flex justify-content-center text-muted">Нет данных для отображения</div>
)

const defaultDataMapper = (data: Array<Record<string, unknown>>) => data

export const TABLE_SELECTOR = 'n2o-advanced-table'
const CLASS_NAME = 'table'

const Widget = ({
    id, disabled, toolbar, datasource, className, setPage, loading,
    fetchData, style, paging, table, size, count, validations,
    page, sorting, children, hasNext, isInit, setResolve,
    changeColumnParam, columnsState, tableConfig, switchTableParam, resetSettings = NOOP_FUNCTION,
    resolvedFilter, resolvedCells, paginationVisible,
    dataMapper = defaultDataMapper, components, setFilter, textWrap,
}: AdvancedTableWidgetProps) => {
    const tableContainerElem = useRef(null)
    const [expandedRows, setExpandedRows] = useState([] as string[])
    const [filterErrors, setFilterErrors] = useState({})

    const datasourceModel = useSelector((state: State) => {
        const model = dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)(state) || EMPTY_ARRAY

        return dataMapper(model)
    }) as Data
    const filterModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.filter))
    const selectedRows = useSelector((state: State) => {
        const models = dataSourceModelByPrefixSelector(datasource, ModelPrefix.selected)(state) as Array<Record<string, unknown>> || EMPTY_ARRAY

        if (models.length) { return models.map(({ id }) => id) }

        return EMPTY_ARRAY
    }) as SelectedRows
    const focusedRowValue = useSelector((state: State) => {
        const model = dataSourceModelByPrefixSelector(datasource, ModelPrefix.active)(state) as { id: string }

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

    const { setActiveModel, setMultiModel, unsetMultiModel, updateDatasource, reorderColumn } = useTableActionReactions(datasource, id)
    const onFilter = useChangeFilter(setFilter, datasource)

    const onRowClickAction = useOnActionMethod(datasource, tableConfig?.body?.row?.click as never)
    const actionListener = useCallback((action, payload) => {
        switch (action) {
            case TableActions.toggleExpandRow: {
                const { rowValue, isOpen } = payload

                if (isOpen) {
                    setExpandedRows(state => [...state, rowValue] as never)
                } else {
                    setExpandedRows(state => state.filter(expandedRow => expandedRow !== rowValue))
                }

                break
            }
            case TableActions.onHeaderDrop: {
                const { id, draggingId, targetId } = payload

                reorderColumn(id, draggingId, targetId)

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
                onRowClickAction(payload.model as never)

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

    useExpandAllRows(setExpandedRows, children as ChildrenToggleState, datasourceModel)

    return (
        <tableWidgetContext.Provider value={{ changeColumnParam, switchTableParam, actionListener, resetSettings }}>
            <VoidResolveColumnConditions
                columnsState={columnsState}
                changeColumnParam={changeColumnParam}
                widgetId={id}
            />
            <ToolbarOverlay
                onClickActionButton={onClickToolbarActionButton}
                refContainerElem={tableContainerElem}
                overlay={tableConfig.body?.row?.overlay}
            >
                <StandardWidget
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
                    {isInit && (
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
                            className={classNames(CLASS_NAME, TABLE_SELECTOR)}
                            id={id}
                        />
                    )}
                </StandardWidget>
            </ToolbarOverlay>
        </tableWidgetContext.Provider>
    )
}

Widget.displayName = 'AdvancedTableComponent'

export const AdvancedTableWidget = WidgetHOC<AdvancedTableWidgetProps>(
    TableSettingsObserver(WithTableProps<AdvancedTableWidgetProps>(Widget)),
)
export default AdvancedTableWidget

AdvancedTableWidget.displayName = 'AdvancedTableWidget'

export const useTableWidget = () => {
    const context = useContextSelector(tableWidgetContext) as tableWidgetContextType & { actionListener(): void }

    if (!context) { throw new Error('useTableWidget must be used with tableWidgetContext') }

    return context
}
