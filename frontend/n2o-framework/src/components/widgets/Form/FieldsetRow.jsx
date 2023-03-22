import React from 'react'
import PropTypes from 'prop-types'
import { pure } from 'recompose'
import { Row } from 'reactstrap'

// eslint-disable-next-line import/no-cycle
import { FieldsetCol } from './FieldsetCol'

function FieldsetRow({ rowId, row, activeModel, parentIndex, ...rest }) {
    return (
        <Row key={rowId} {...row.props} className={row.className} style={row.style}>
            {row.cols &&
                row.cols.map((col, colId) => (
                    <FieldsetCol
                        // eslint-disable-next-line react/no-array-index-key
                        key={colId}
                        col={col}
                        colId={colId}
                        activeModel={{ ...activeModel, index: parentIndex }}
                        parentIndex={parentIndex}
                        {...rest}
                    />
                ))}
        </Row>
    )
}

FieldsetRow.propTypes = {
    rowId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    row: PropTypes.object,
    colId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    parentIndex: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    activeModel: PropTypes.object,
}

export default pure(FieldsetRow)
