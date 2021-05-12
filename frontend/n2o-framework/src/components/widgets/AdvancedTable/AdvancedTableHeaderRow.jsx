import React from 'react'
import { pure } from 'recompose'

/**
 * Компонент создания строки заголовка
 * @param props
 * @constructor
 */
function AdvancedTableHeaderRow(props) {
    const rowProps = {
        ...props,
        className: 'n2o-advanced-table-header-row',
    }

    return React.createElement('tr', rowProps, [...props.children])
}

export default pure(AdvancedTableHeaderRow)
