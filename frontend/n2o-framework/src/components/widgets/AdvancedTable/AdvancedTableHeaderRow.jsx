import React from 'react'
import { pure } from 'recompose'

/**
 * Компонент создания строки заголовка
 * @param props
 * @constructor
 */
function AdvancedTableHeaderRow(props) {
    // eslint-disable-next-line react/prop-types
    const { children } = props
    const rowProps = {
        ...props,
        className: 'n2o-advanced-table-header-row',
    }

    return React.createElement('tr', rowProps, [...children])
}

export default pure(AdvancedTableHeaderRow)
