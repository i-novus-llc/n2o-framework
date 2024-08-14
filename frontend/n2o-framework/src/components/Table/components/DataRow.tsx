import React, { FC, useCallback } from 'react'

import { DataRowProps } from '../types/props'

import Table from './basic'

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
        <Table.Row
            {...otherProps}
            onClick={() => onRowClick(data)}
        >
            {children}
        </Table.Row>
    )
}
