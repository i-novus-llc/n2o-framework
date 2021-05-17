import React from 'react'
import pure from 'recompose/pure'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import some from 'lodash/some'
import classNames from 'classnames'

/**
 * Компонент обертка Cell
 * @param children - вставляемый компонент
 * @param hasSpan - флаг возможности colSpan/rowSpan в этой колонке
 * @param record - модель строки
 * @param textWrap - флаг на запрет/разрешение переноса текста в cell (default = true)
 * @returns {*}
 * @constructor
 */
function AdvancedTableCell({ children, hasSpan, record, textWrap }) {
    const { span } = record
    let colSpan = 1
    let rowSpan = 1

    if (hasSpan && span) {
        if (span.colSpan === 0 || span.rowSpan === 0) {
            return null
        }
        colSpan = span.colSpan
        rowSpan = span.rowSpan
    }

    const needRender = some(children, child => get(child, 'props.needRender', true))

    return (
        <td
            className={classNames({ 'd-none': !needRender })}
            colSpan={colSpan}
            rowSpan={rowSpan}
        >
            <div
                className={classNames('n2o-advanced-table-cell-expand', {
                    'text-no-wrap': textWrap === false,
                })}
            >
                {children}
            </div>
        </td>
    )
}

AdvancedTableCell.propTypes = {
    children: PropTypes.any,
    textWrap: PropTypes.bool,
    hasSpan: PropTypes.bool,
    record: PropTypes.object,
}

AdvancedTableCell.defaultProps = {
    hasSpan: false,
    record: {},
    textWrap: true,
}

export default pure(AdvancedTableCell)
