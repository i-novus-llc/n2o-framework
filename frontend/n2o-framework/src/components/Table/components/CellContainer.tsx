import React, { memo } from 'react'
import get from 'lodash/get'
import classNames from 'classnames'

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

    const currentCellType = get(otherProps.model, otherProps.switchFieldId)
    const cellProps = get(otherProps.switchList, currentCellType, otherProps.switchDefault)
    const cellAttributes = cellProps?.elementAttributes

    return (
        <Table.Cell
            className={otherProps.id === 'selectionCell' ? 'cell-selection' : ''}
            align={alignment}
        >
            <div className={classNames('cell-content', cellAttributes?.className)}>
                {cellIndex === 0 && hasExpandedButton && (
                    <ExpandButton
                        rowValue={rowValue}
                        isTreeExpanded={isTreeExpanded}
                    />
                )}
                <CellComponent rowValue={rowValue} {...resolvedProps} rowIndex={rowIndex} style={style} />
            </div>
        </Table.Cell>
    )
})

CellContainer.displayName = 'CellContainer'
