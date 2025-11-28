/* eslint-disable sonarjs/cognitive-complexity, complexity */
// FIXME необходим рефакторинг компонента
import React, { useEffect, useContext } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'

import { useData } from '../../../core/widget/useData'
import { makeTableByIdSelector } from '../../../ducks/table/selectors'
import { registerTable, updateTableParams } from '../../../ducks/table/store'
import { dataSourcePagingSelector, dataSourceSortingSelector } from '../../../ducks/datasource/selectors'
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

        const { cells: headerCells = [] } = header || {}
        const { cells: bodyCells = [] } = body || {}

        // синхронизация сохраненных настроек
        useEffect(() => {
            if (!saveSettings) { return }

            // сброс и установка default
            if (isEmpty(cachedSettings)) {
                // @ts-ignore FIXME скорее всего будет не в этом месте, пока не типизирую
                const { defaultProps, defaultDatasourceProps } = table || {}

                if (defaultProps) {
                    // overwrite true иначе редюсер смержит параметры в header/body.cells
                    dispatch(registerTable(id, defaultProps, true))
                }

                if (defaultDatasourceProps) {
                    const { paging: defaultPaging, sorting: defaultSorting } = defaultDatasourceProps

                    fetchData(defaultPaging.page ? defaultPaging : { ...defaultPaging, page: 1 })

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

                if (savedPaging && !isEmpty(savedPaging) && !isEqual(savedPaging, paging)) {
                    if (!isEqual(savedPaging.size, size)) {
                        setSize(savedPaging.size)

                        return
                    }

                    if (!isEqual(savedPaging.page, page)) {
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
