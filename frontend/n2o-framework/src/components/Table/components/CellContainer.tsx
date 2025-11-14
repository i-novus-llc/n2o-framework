import React, { memo } from 'react'
import get from 'lodash/get'
import classNames from 'classnames'

import { useResolved } from '../../../core/Expression/useResolver'
import { type CellContainerProps } from '../types/props'

import { ExpandButton } from './ExpandButton'

export const CellContainer = memo<CellContainerProps>((props) => {
    const {
        component: CellComponent,
        cellIndex,
        hasExpandedButton,
        isTreeExpanded,
        rowValue,
        rowIndex,
        elementAttributes = {},
        ...rest
    } = props
    const resolvedProps = useResolved(rest, rest.model, ['toolbar', 'security', 'model', 'content'])

    const currentCellType = get(rest.model, rest.switchFieldId)
    const cellProps = get(rest.switchList, currentCellType, rest.switchDefault)
    const cellAttributes = cellProps?.elementAttributes

    return (
        <td
            className={classNames(elementAttributes.className, { 'cell-selection': rest.id === 'selectionCell' })}
            align={elementAttributes.alignment}
            style={elementAttributes.style}
        >
            <div className={classNames('cell-content', cellAttributes?.className)}>
                {cellIndex === 0 && hasExpandedButton && (
                    <ExpandButton
                        rowValue={rowValue}
                        isTreeExpanded={isTreeExpanded}
                    />
                )}
                <CellComponent rowValue={rowValue} {...resolvedProps} rowIndex={rowIndex} />
            </div>
        </td>
    )
})

CellContainer.displayName = 'CellContainer'
