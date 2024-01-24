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
}

const tableActionsContext = createContext<TableActionContextValue | null>(null)

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
        throw Error('useTableActions must be used in TableActionsProvider')
    }

    return context
}
