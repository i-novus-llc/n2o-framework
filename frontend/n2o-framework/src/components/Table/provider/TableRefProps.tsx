import { createContext, useContext } from 'use-context-selector'
import React, { FC, useRef } from 'react'

import { TableWidgetContainerProps } from '../models/props'

const tablePropsContext = createContext<React.MutableRefObject<TableWidgetContainerProps> | null>(null)

export const TableRefProps: FC<{ value: TableWidgetContainerProps }> = ({ value, children }) => {
    const refProps = useRef(value)

    refProps.current = value

    return (
        <tablePropsContext.Provider value={refProps}>
            {children}
        </tablePropsContext.Provider>
    )
}

export const useTableRefProps = () => {
    const context = useContext(tablePropsContext)

    if (!context) {
        throw Error('useTableRefProps must be used in TableRefProps')
    }

    return context
}
