/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { FC, useMemo } from 'react'
import { createContext, useContext } from 'use-context-selector'

import { TableActions } from '../enum'
import { NOOP_FUNCTION } from '../../../utils/emptyTypes'

type TableActionContextValue = {
    toggleExpandRow(rowValue: string, isOpen: boolean): void
    selectRows(rowValues: string[]): void
    deselectRows(rowValues: string[]): void
    selectSingleRow(rowValue: string): void
    setFocusOnRow(rowValue: string | null, model?: any): void
    onRowClick(model: any): void
    onChangeFilter(model: Record<string, any>): void
    onUpdateModel(model: Record<string, any>, rowIndex: number): void
    onHeaderDrop(id: string, draggingId: string, targetId: string): void
}

const MESSAGE = 'Need an implementation method\''

const tableActionsContext = createContext<TableActionContextValue>({
    toggleExpandRow() {
        console.warn(MESSAGE)
    },
    selectRows() {
        console.warn(MESSAGE)
    },
    deselectRows() {
        console.warn(MESSAGE)
    },
    selectSingleRow() {
        console.warn(MESSAGE)
    },
    setFocusOnRow() {
        console.warn(MESSAGE)
    },
    onRowClick() {
        console.warn(MESSAGE)
    },
    onChangeFilter() {
        console.warn(MESSAGE)
    },
    onUpdateModel() {
        console.warn(MESSAGE)
    },
    onHeaderDrop() {
        console.warn(MESSAGE)
    },
})

type TableActionsProviderProps = {
    actionListener(action: TableActions, payload: any): void
}

export const TableActionsProvider: FC<TableActionsProviderProps> = ({
    actionListener = NOOP_FUNCTION,
    children,
}) => {
    const methods = useMemo<TableActionContextValue>(() => ({
        toggleExpandRow(rowValue, isOpen) {
            actionListener(TableActions.toggleExpandRow, { rowValue, isOpen })
        },
        selectRows(listRowValue) {
            actionListener(TableActions.selectRows, { listRowValue })
        },
        deselectRows(listRowValue) {
            actionListener(TableActions.deselectRows, { listRowValue })
        },
        selectSingleRow(rowValue) {
            actionListener(TableActions.selectSingleRow, { rowValue })
        },
        setFocusOnRow(rowValue, model) {
            actionListener(TableActions.setFocusOnRow, { rowValue, model })
        },
        onRowClick(model) {
            actionListener(TableActions.onRowClick, { model })
        },
        onChangeFilter(model) {
            actionListener(TableActions.onChangeFilter, { model })
        },
        onUpdateModel(model, rowIndex) {
            actionListener(TableActions.onUpdateModel, { model, rowIndex })
        },
        onHeaderDrop(id: string, draggingId: string, targetId: string) {
            actionListener(TableActions.onHeaderDrop, { id, draggingId, targetId })
        },
    }), [actionListener])

    return (
        <tableActionsContext.Provider value={{ ...methods }}>
            {children}
        </tableActionsContext.Provider>
    )
}

TableActionsProvider.displayName = 'TableActionsProvider'

export const useTableActions = () => {
    const context = useContext(tableActionsContext)

    if (!context) {
        console.warn('useTableActions must be used in TableActionsProvider')
    }

    return context
}
