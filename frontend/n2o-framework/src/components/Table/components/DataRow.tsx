import React, { FC, useCallback } from 'react'

import { DataRowProps } from '../types/props'

export const DataRow: FC<DataRowProps> = ({ children, onSelection, onClick, data, ...otherProps }) => {
    const onRowClick = useCallback((data) => {
        if (onSelection) {
            onSelection(data)
        }

        if (onClick) {
            onClick(data)
        }
    }, [onSelection, onClick])

    return (
        <tr
            {...otherProps}
            onClick={() => onRowClick(data)}
        >
            {children}
        </tr>
    )
}

DataRow.displayName = 'DataRow'
