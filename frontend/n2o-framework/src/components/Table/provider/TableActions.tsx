/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { FC, useMemo } from 'react'
import { createContext, useContext } from 'use-context-selector'

import { useOnActionMethod } from '../../widgets/hooks/useOnActionMethod'
import { TableActions } from '../enum'

type TTableActionContextValue = {
    toggleExpandRow(rowValue: string, isOpen: boolean): void
    selectRows(rowValues: string[]): void
    deselectRows(rowValues: string[]): void
    selectSingleRow(rowValue: string): void
    setFocusOnRow(rowValue: string | null, model?: any): void
    onDispatchRowAction(rowClickAction: Record<string, any>, model: any): void
    onChangeFilter(model: Record<string, any>): void
}

const tableActionsContext = createContext<TTableActionContextValue | null>(null)

type TTableActionsProviderProps = {
    actionListener(action: TableActions, payload: any): void
    widgetId: string
}

export const TableActionsProvider: FC<TTableActionsProviderProps> = ({
    actionListener,
    children,
    widgetId,
}) => {
    const onRowClickAction = useOnActionMethod(widgetId)
    const methods = useMemo<TTableActionContextValue>(() => ({
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
        onDispatchRowAction(rowClickAction, model) {
            onRowClickAction(model, rowClickAction)
        },
        onChangeFilter(model) {
            actionListener(TableActions.onChangeFilter, { model })
        },
    }), [actionListener, onRowClickAction])

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
