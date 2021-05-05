import React from 'react'
import { compose, withHandlers, pure } from 'recompose'
import PropTypes from 'prop-types'
import cn from 'classnames'

/**
 * Компонент кнопки открытия подстроки
 * @param record - модель строки
 * @param expanded - флаг открытия
 * @param onExpand - callback на открытие
 * @param expandedFieldId - ключ с expandableContent
 * @param expandedComponent - кастомный компонент
 * @returns {*}
 * @constructor
 */
function AdvancedTableExpandIcon({
    record,
    expanded,
    onExpand,
    expandedFieldId,
    expandedComponent,
}) {
    return (
        <span
            className={cn({
                'n2o-advanced-table-expand':
          record.children || record[expandedFieldId] || expandedComponent,
            })}
            onClick={onExpand}
        >
            {(record[expandedFieldId] || record.children || expandedComponent) && (
                <i
                    className={cn('fa', 'n2o-advanced-table-expand-icon', {
                        'n2o-advanced-table-expand-icon-expanded': expanded,
                        'fa-angle-right': !expanded,
                        'fa-angle-down': expanded,
                    })}
                />
            )}
        </span>
    )
}

AdvancedTableExpandIcon.propTypes = {
    record: PropTypes.object,
    expanded: PropTypes.bool,
    onExpand: PropTypes.func,
}

AdvancedTableExpandIcon.defaultProps = {
    record: {},
}

const enhance = compose(
    withHandlers({
        onExpand: ({ onExpand, record }) => e => onExpand(record, e),
    }),
    pure,
)

export { AdvancedTableExpandIcon }
export default enhance(AdvancedTableExpandIcon)
