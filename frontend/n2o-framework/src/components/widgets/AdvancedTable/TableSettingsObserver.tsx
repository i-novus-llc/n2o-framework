import React, { useEffect, useContext } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import omit from 'lodash/omit'
import isEqual from 'lodash/isEqual'

import { useData } from '../../../core/widget/useData'
import { makeTableByIdSelector } from '../../../ducks/table/selectors'
import { registerTable, updateTableParams } from '../../../ducks/table/store'
import { type BodyCell, type HeaderCell } from '../../../ducks/table/Table'
import { dataSourcePagingSelector, dataSourceSortingSelector } from '../../../ducks/datasource/selectors'
import { SortDirection } from '../../../core/datasource/const'
import { DataSourceContext } from '../../../core/widget/context'

import { createColumns, prepareCellsToStore } from './helpers'
import { DATA_SOURCE_SAVED_SETTINGS, type DatasourceSavedSettings, SAVED_SETTINGS, type WithTableType } from './types'

const EMPTY_CELLS = { cells: [] }

export function TableSettingsObserver<P extends WithTableType>(Component: React.ComponentType<P>) {
    function Wrapper(props: P) {
        const dispatch = useDispatch()

        const { fetchData, setSorting, setSize, setPage } = useContext(DataSourceContext)

        const { id, datasource, size, page, saveSettings } = props
        const { value, setValue } = useData(id)

        const resetSettings = () => setValue(null)

        // TODO таблица приходит из widget, нужно что бы приходила из redux table
        const table = useSelector(makeTableByIdSelector(id))
        const { header = EMPTY_CELLS, body = EMPTY_CELLS, textWrap } = table
        const paging = useSelector(dataSourcePagingSelector(datasource))
        const sorting = useSelector(dataSourceSortingSelector(datasource))

        const { cells: headerCells = [] } = header || {}
        const { cells: bodyCells = [] } = body || {}

        // синхронизация сохраненных настроек
        useEffect(() => {
            if (!saveSettings) { return }

            // сброс и установка default
            if (isEmpty(value)) {
                // @ts-ignore FIXME скорее всего будет не в этом месте, пока не типизирую
                const { defaultProps, defaultDatasourceProps } = table || {}

                if (defaultProps) {
                    // overwrite true иначе редюсер смержит параметры в header/body.cells
                    dispatch(registerTable(id, defaultProps, true))
                }

                if (defaultDatasourceProps) {
                    const { paging: defaultPaging, sorting: defaultSorting } = defaultDatasourceProps

                    fetchData(defaultPaging)

                    if (isEmpty(defaultSorting)) {
                        if (!isEmpty(sorting)) {
                            const [[id]] = Object.entries(sorting)

                            setSorting(id, SortDirection.none)

                            return
                        }

                        return
                    }

                    const [[id]] = Object.entries(defaultSorting)

                    setSorting(id, SortDirection.none)
                }

                return
            }

            // синхронизация между вкладками
            const computedHeaderCells = createColumns<HeaderCell>(headerCells, value[SAVED_SETTINGS.HEADER] as HeaderCell[])
            const computedBodyCells = createColumns<BodyCell>(bodyCells, value[SAVED_SETTINGS.BODY] as BodyCell[])

            dispatch(updateTableParams(id, {
                ...omit(value, SAVED_SETTINGS.DATA_SOURCE_SETTINGS),
                header: { cells: computedHeaderCells },
                body: { cells: computedBodyCells },
            }))

            const datasourceFeatures = value[SAVED_SETTINGS.DATA_SOURCE_SETTINGS] as DatasourceSavedSettings

            if (!isEmpty(datasourceFeatures)) {
                const { sorting: savedSorting, paging: savedPaging } = datasourceFeatures

                if (!isEqual(savedSorting, sorting)) {
                    // Синхронизация с другой вкладки, перебивает sorting из props
                    if (isEmpty(savedSorting) && !isEmpty(sorting)) {
                        const [[id, direction]] = Object.entries(sorting)

                        setSorting(id, SortDirection.none)
                    } else if (!isEmpty(savedSorting)) {
                        const [[id, direction]] = Object.entries(savedSorting)

                        setSorting(id, direction)
                    }
                }

                if (!isEmpty(savedPaging) && !isEqual(savedPaging, paging)) {
                    if (!isEqual(savedPaging.size, size)) {
                        setSize(savedPaging.size)

                        return
                    }

                    if (!isEqual(savedPaging.page, page)) {
                        setPage(savedPaging.page)
                    }
                }
            }
        }, [value])

        // сохранение настроек
        useEffect(() => {
            if (!saveSettings) { return }

            if (!isEmpty(headerCells) && !isEmpty(bodyCells)) {
                setValue({
                    [SAVED_SETTINGS.HEADER]: prepareCellsToStore(headerCells),
                    [SAVED_SETTINGS.BODY]: prepareCellsToStore(bodyCells),
                    [SAVED_SETTINGS.TEXT_WRAP]: textWrap,
                    [SAVED_SETTINGS.DATA_SOURCE_SETTINGS]: {
                        [DATA_SOURCE_SAVED_SETTINGS.SORTING]: sorting,
                        [DATA_SOURCE_SAVED_SETTINGS.PAGING]: paging,
                    },
                })
            }
        }, [headerCells, bodyCells, sorting, textWrap, paging])

        const computedTable = { ...table, header, body }

        return <Component {...props} table={computedTable} resetSettings={resetSettings} />
    }

    return Wrapper
}
