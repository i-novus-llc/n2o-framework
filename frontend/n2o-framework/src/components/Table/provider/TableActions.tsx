/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { FC, useMemo } from 'react'
import { createContext, useContext } from 'use-context-selector'

import { TableActions } from '../enum'

type TableActionContextValue = {
    toggleExpandRow(rowValue: string, isOpen: boolean): void
    selectRows(rowValues: string[]): void
    deselectRows(rowValues: string[]): void
    selectSingleRow(rowValue: string): void
    setFocusOnRow(rowValue: string | null, model?: any): void
    onRowClick(model: any): void
    onChangeFilter(model: Record<string, any>): void
    onUpdateModel(model: Record<string, any>, rowIndex: number): void
}

const tableActionsContext = createContext<TableActionContextValue>({
    toggleExpandRow() {
        console.warn('Need an implementation method')
    },
    selectRows() {
        console.warn('Need an implementation method')
    },
    deselectRows() {
        console.warn('Need an implementation method')
    },
    selectSingleRow() {
        console.warn('Need an implementation method')
    },
    setFocusOnRow() {
        console.warn('Need an implementation method')
    },
    onRowClick() {
        console.warn('Need an implementation method')
    },
    onChangeFilter() {
        console.warn('Need an implementation method')
    },
    onUpdateModel() {
        console.warn('Need an implementation method')
    },
})

type TableActionsProviderProps = {
    actionListener(action: TableActions, payload: any): void
}

export const TableActionsProvider: FC<TableActionsProviderProps> = ({
    actionListener,
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
    }), [actionListener])

    return (
        <tableActionsContext.Provider value={{ ...methods }}>
            {children}
        </tableActionsContext.Provider>
    )
}

TableActionsProvider.displayName = 'TableActionsProvider'
TableActionsProvider.defaultProps = {
    actionListener: () => {},
}

export const useTableActions = () => {
    const context = useContext(tableActionsContext)

    if (!context) {
        console.warn('useTableActions must be used in TableActionsProvider')
    }

    return context
}
