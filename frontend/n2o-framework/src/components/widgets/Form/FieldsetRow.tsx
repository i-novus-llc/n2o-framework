import React, { memo } from 'react'
import { Row } from 'reactstrap'

import { FieldsetCol } from './FieldsetCol'
import { type FieldSetRowComponentProps } from './types'

function FieldsetRow({ rowId, row, activeModel, ...rest }: FieldSetRowComponentProps) {
    return (
        <Row key={rowId} {...row.props} className={row.className} style={row.style}>
            {row?.cols &&
                row.cols.map((col, colId) => (
                    <FieldsetCol
                        // eslint-disable-next-line react/no-array-index-key
                        key={colId}
                        col={col}
                        colId={colId}
                        activeModel={activeModel}
                        {...rest}
                    />
                ))}
        </Row>
    )
}

export default memo(FieldsetRow)
