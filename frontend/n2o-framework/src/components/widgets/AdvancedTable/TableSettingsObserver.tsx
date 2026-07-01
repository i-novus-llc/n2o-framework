import React, { useEffect, useRef } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import isEmpty from 'lodash/isEmpty'

import { useData } from '../../../core/widget/useData'
import { makeTableByIdSelector } from '../../../ducks/table/selectors'
import { registerTable, updateTableParams } from '../../../ducks/table/store'

import { mergeWithCache } from './helpers'
import { TableProps, TableStateCache, type WithTableType } from './types'

const EMPTY_CELLS = { cells: [] }

// Обновляет данные таблицы в зависимости от локальных настроек
export function TableSettingsObserver<P extends WithTableType>(Component: React.ComponentType<P>) {
    function Wrapper(props: P) {
        const dispatch = useDispatch()

        const { id, saveSettings } = props

        const { value: cachedSettings, setValue: updateCache } = useData<TableStateCache>(id)
        const prevCachedSettingsRef = useRef(cachedSettings)

        // TODO таблица приходит из widget, нужно что бы приходила из redux table
        const table = useSelector(makeTableByIdSelector(id))

        const registerDefaultTableProps = () => {
            const { defaultProps } = table || {}

            if (defaultProps) {
                // overwrite true иначе редюсер смержит параметры в header/body.cells
                dispatch(registerTable(id, defaultProps, true))
            }
        }

        const resetSettings = () => {
            updateCache(null)
            registerDefaultTableProps()
        }

        const { header = EMPTY_CELLS, body = EMPTY_CELLS } = table

        const { cells: headerCells = [] } = header || {}
        const { cells: bodyCells = [] } = body || {}

        // синхронизация сохраненных настроек
        useEffect(() => {
            if (!saveSettings) { return }

            const prevCached = prevCachedSettingsRef.current
            const shouldReset = !isEmpty(prevCached) && isEmpty(cachedSettings)

            // регистрация таблицы default props при сбросе локальных данных
            if (shouldReset) {
                registerDefaultTableProps()
                prevCachedSettingsRef.current = cachedSettings

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
            prevCachedSettingsRef.current = cachedSettings
        }, [cachedSettings])

        const computedTable = { ...table, header, body }

        return <Component {...props} table={computedTable} resetSettings={resetSettings} />
    }

    return Wrapper
}
