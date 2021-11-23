import React from 'react'
import PropTypes from 'prop-types'

import WidgetLayout from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/Widget'
import { widgetPropTypes } from '../../../core/widget/propTypes'

import TreeContainer from './container/TreeContainer'

function TreeWidget(props) {
    const {
        id: widgetId,
        datasource,
        toolbar,
        disabled,
        className,
        style,
    } = props

    return (
        <WidgetLayout
            disabled={disabled}
            widgetId={widgetId}
            datasource={datasource}
            toolbar={toolbar}
            className={className}
            style={style}
        >
            <TreeContainer
                {...props}
            />
        </WidgetLayout>
    )
}

TreeWidget.propTypes = {
    ...widgetPropTypes,
    hasFocus: PropTypes.bool,
    hasSelect: PropTypes.bool,
    autoFocus: PropTypes.any,
    rowClick: PropTypes.func,
    childIcon: PropTypes.string,
    multiselect: PropTypes.bool,
    showLine: PropTypes.bool,
    expandBtn: PropTypes.bool,
    bulkData: PropTypes.bool,
    parentFieldId: PropTypes.string,
    valueFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    iconFieldId: PropTypes.string,
    imageFieldIdd: PropTypes.string,
    badgeFieldId: PropTypes.string,
    badgeColorFieldId: PropTypes.string,
    hasCheckboxes: PropTypes.bool,
    draggable: PropTypes.bool,
    childrenFieldId: PropTypes.string,
    placeholder: PropTypes.string,
}

const Widget = WidgetHOC(TreeWidget)

Widget.defaultProps = {
    fetchOnInit: true,
}

export default Widget
