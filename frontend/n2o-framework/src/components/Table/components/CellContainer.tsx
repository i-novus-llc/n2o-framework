import React, { memo, useMemo } from 'react'

// @ts-ignore - отсутствуют типы
import propsResolver from '../../../utils/propsResolver'
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
        ...otherProps
    } = props
    const resolvedProps = useMemo(() => propsResolver(otherProps, otherProps.model, ['toolbar', 'security', 'model']), [otherProps])

    return (
        <Table.Cell
            className={otherProps.id === 'selectionCell' ? 'cell-selection' : ''}
            align={alignment}
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
