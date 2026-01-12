/* eslint-disable sonarjs/cognitive-complexity, complexity */
// FIXME необходим рефакторинг компонента
import React, { useEffect, useContext } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'

import { useData } from '../../../core/widget/useData'
import { makeTableByIdSelector } from '../../../ducks/table/selectors'
import { registerTable, updateTableParams } from '../../../ducks/table/store'
import {
    dataSourceDefaultPropsSelector,
    dataSourcePagingSelector,
    dataSourceSortingSelector,
} from '../../../ducks/datasource/selectors'
import { SortDirection } from '../../../core/datasource/const'
import { DataSourceContext } from '../../../core/widget/context'

import { mergeWithCache } from './helpers'
import { TableProps, TableStateCache, type WithTableType } from './types'

const EMPTY_CELLS = { cells: [] }

export function TableSettingsObserver<P extends WithTableType>(Component: React.ComponentType<P>) {
    function Wrapper(props: P) {
        const dispatch = useDispatch()

        const { fetchData, setSorting, setSize, setPage } = useContext(DataSourceContext)

        const { id, datasource, size, page, saveSettings } = props
        const { value: cachedSettings, setValue: updateCache } = useData<TableStateCache>(id)

        const resetSettings = () => updateCache(null)

        // TODO таблица приходит из widget, нужно что бы приходила из redux table
        const table = useSelector(makeTableByIdSelector(id))
        const { header = EMPTY_CELLS, body = EMPTY_CELLS } = table
        const paging = useSelector(dataSourcePagingSelector(datasource))
        const sorting = useSelector(dataSourceSortingSelector(datasource))

        const defaultDatasourceProps = useSelector(dataSourceDefaultPropsSelector(datasource))

        const { cells: headerCells = [] } = header || {}
        const { cells: bodyCells = [] } = body || {}

        // синхронизация сохраненных настроек
        useEffect(() => {
            if (!saveSettings) { return }

            // сброс локальных настроек и установка default
            if (isEmpty(cachedSettings)) {
                const { defaultProps } = table || {}

                if (defaultProps) {
                    // overwrite true иначе редюсер смержит параметры в header/body.cells
                    dispatch(registerTable(id, defaultProps, true))
                }

                // TODO DataSourceSettingsObserver
                // нужен механизм для datasource который будет заниматься get/set своих локальных настроек
                // table observer должен работать только со строками таблицы
                if (defaultDatasourceProps) {
                    const { paging: defaultPaging, sorting: defaultSorting } = defaultDatasourceProps

                    fetchData(defaultPaging?.page ? defaultPaging : { ...defaultPaging, page: 1 })

                    if ((isEmpty(defaultSorting) || defaultSorting === undefined) && !isEmpty(sorting)) {
                        const [[id]] = Object.entries(sorting)

                        setSorting(id, SortDirection.none)
                    } else if (defaultSorting) {
                        const [[id, direction]] = Object.entries(defaultSorting)

                        setSorting(id, direction || SortDirection.none)
                    }

                    if (defaultPaging) {
                        setPage(defaultPaging?.page || 1)
                        setSize(defaultPaging.size)
                    }
                }

                return
            }

            // синхронизация между вкладками
            const updatedParams: Partial<TableProps> = {
                header: { cells: mergeWithCache(headerCells, cachedSettings.header) },
                body: { cells: mergeWithCache(bodyCells, cachedSettings.body) },
            }

            if (cachedSettings.textWrap !== undefined) {
                updatedParams.textWrap = cachedSettings.textWrap
            }

            dispatch(updateTableParams(id, updatedParams))

            const { datasourceFeatures } = cachedSettings

            if (datasourceFeatures && !isEmpty(datasourceFeatures)) {
                const { sorting: savedSorting = {}, paging: savedPaging } = datasourceFeatures

                // Синхронизация с другой вкладки, перебивает sorting из props
                if (isEmpty(savedSorting) && !isEmpty(sorting)) {
                    const [[id, direction]] = Object.entries(sorting)

                    setSorting(id, SortDirection.none)
                } else if (!isEmpty(savedSorting)) {
                    const [[id, direction]] = Object.entries(savedSorting)

                    setSorting(id, direction)
                } else if (isEmpty(savedSorting) && isEmpty(sorting)) {
                    // @INFO Если сортировка неизвестна, приходится обходить все колонки и устанавливать direction: none.
                    // Так как это может указывать на отключение локальной сортировки. Иначе dataSource выставит свой default.
                    props.table.header.cells.forEach(({ id }) => {
                        setSorting(id, SortDirection.none)
                    })
                }

                if (savedPaging && !isEmpty(savedPaging) && !isEqual(savedPaging, paging)) {
                    if (savedPaging.size !== size) {
                        setSize(savedPaging.size)
                    }

                    if (savedPaging.page !== page) {
                        setPage(savedPaging.page)
                    }
                }
            }
        }, [cachedSettings])

        const computedTable = { ...table, header, body }

        return <Component {...props} table={computedTable} resetSettings={resetSettings} />
    }

    return Wrapper
}
