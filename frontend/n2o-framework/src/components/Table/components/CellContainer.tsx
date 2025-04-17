import React, { memo } from 'react'
import get from 'lodash/get'
import classNames from 'classnames'

import { useResolved } from '../../../core/Expression/useResolver'
import { CellContainerProps } from '../types/props'
import { EMPTY_OBJECT } from '../../../utils/emptyTypes'

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
        style = EMPTY_OBJECT,
        ...rest
    } = props
    const resolvedProps = useResolved(rest, rest.model, ['toolbar', 'security', 'model', 'content'])

    const currentCellType = get(rest.model, rest.switchFieldId)
    const cellProps = get(rest.switchList, currentCellType, rest.switchDefault)
    const cellAttributes = cellProps?.elementAttributes

    return (
        <Table.Cell
            className={rest.id === 'selectionCell' ? 'cell-selection' : ''}
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
