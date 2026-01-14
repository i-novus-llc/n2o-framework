import React, { FC } from 'react'

import { DataRowProps } from '../types/props'
import { useClickWithoutSelection } from '../../../utils/useClickWithoutSelection'

export const DataRow: FC<DataRowProps> = ({ children, onSelection, onClick, data, ...otherProps }) => {
    const onRowClick = useClickWithoutSelection(() => {
        if (onSelection) { onSelection(data) }

        if (onClick) { onClick(data) }
    })

    return (
        <tr
            {...otherProps}
            onClick={onRowClick}
        >
            {children}
        </tr>
    )
}

DataRow.displayName = 'DataRow'
