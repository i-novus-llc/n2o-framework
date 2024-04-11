import React, { memo } from 'react'

import { useResolved } from '../../../core/Expression/useResolver'
import { CellContainerProps } from '../types/props'

import Table from './basic'
import { ExpandButton } from './ExpandButton'

export const CellContainer = memo<CellContainerProps>((props) => {
    const {
        component: CellComponent,
        cellIndex,
        hasExpandedButton,
        isTreeExpanded,
        rowValue,
        rowIndex,
        alignment,
        style,
        ...otherProps
    } = props
    const resolvedProps = useResolved(otherProps, otherProps.model, ['toolbar', 'security', 'model'])

    return (
        <Table.Cell
            className={otherProps.id === 'selectionCell' ? 'cell-selection' : ''}
            align={alignment}
            style={style}
        >
            <div className="cell-content">
                {cellIndex === 0 && hasExpandedButton && (
                    <ExpandButton
                        rowValue={rowValue}
                        isTreeExpanded={isTreeExpanded}
                    />
                )}
                <CellComponent rowValue={rowValue} {...resolvedProps} rowIndex={rowIndex} />
            </div>
        </Table.Cell>
    )
})

CellContainer.displayName = 'CellContainer'
