import PropTypes from 'prop-types'
import { omit } from 'lodash'

import { widgetInitialTypes, widgetPropTypes } from '../../../core/widget/propTypes'
import { pagingType } from '../../snippets/Pagination/types'

export const WidgetTableTypes = {
    autoCheckboxOnSelect: PropTypes.bool,
    cells: PropTypes.array,
    hasFocus: PropTypes.bool,
    hasSelect: PropTypes.bool,
    headers: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.string.isRequired,
        conditions: PropTypes.any, // TODO fill
        label: PropTypes.string,
        sortingParam: PropTypes.string,
        src: PropTypes.string.isRequired,
    })),
    rowSelection: PropTypes.oneOf(['none', 'active', 'radio', 'checkbox']),
    tableSize: PropTypes.string,
    width: PropTypes.string,
    height: PropTypes.string,
    rowClick: PropTypes.shape({
        url: PropTypes.string,
        target: PropTypes.oneOf(['self', '_blank', 'application']),
        pathMapping: PropTypes.any,
        queryMapping: PropTypes.any,
        action: PropTypes.any,
        enablingCondition: PropTypes.string,
    }),
    rows: PropTypes.object,
    rowClass: PropTypes.string,
    multiHeader: PropTypes.bool,
}

export const AdvancedTableInitialTypes = {
    ...widgetInitialTypes,
    table: PropTypes.shape(WidgetTableTypes).isRequired,
    paging: pagingType,
}

export const AdvancedTableWidgetTypes = {
    ...widgetPropTypes,
    ...AdvancedTableInitialTypes,
    expandable: PropTypes.bool, // TODO remove
    expandedFieldId: PropTypes.string, // TODO remove
}

export const AdvancedTableContainerTypes = {
    ...omit(AdvancedTableWidgetTypes, ['table']),
    cells: PropTypes.arrayOf(PropTypes.element),
    headers: PropTypes.arrayOf(PropTypes.element),
}
