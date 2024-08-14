import React from 'react'
import PropTypes from 'prop-types'
import { Table as BaseTable } from 'reactstrap'

import { mapToNumOrStr } from '../../utils'

export function Table({ cols, rows }) {
    const renderTh = () => (
        <th aria-label="placeholder-content">
            <div className="n2o-placeholder-content" />
        </th>
    )

    const renderTd = () => (
        <td>
            <div className="n2o-placeholder-content" />
        </td>
    )

    const renderTr = () => <tr>{mapToNumOrStr(cols, renderTd)}</tr>

    return (
        <BaseTable>
            <thead>
                <tr>{mapToNumOrStr(cols, renderTh)}</tr>
            </thead>
            <tbody>{mapToNumOrStr(rows, renderTr)}</tbody>
        </BaseTable>
    )
}

Table.propTypes = {
    /**
     * Количество столбцов
     */
    cols: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
     * Количество строк
     */
    rows: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
}

Table.defaultProps = {
    cols: 2,
    rows: 2,
}

export default Table
