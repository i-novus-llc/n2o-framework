import React from 'react'

import StandardWidget, { type Props as StandardWidgetProps } from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { WithActiveModel } from '../Widget/WithActiveModel'

import TreeContainer from './container/TreeContainer'
import { type TreeProps, type WithWidgetHandlersProps } from './types'

type TreeWidgetProps = Omit<StandardWidgetProps, 'filter'> & TreeProps & WithWidgetHandlersProps

function Widget(props: TreeWidgetProps) {
    const {
        id: widgetId,
        datasource,
        toolbar,
        disabled,
        className,
        style,
        loading,
    } = props

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            datasource={datasource}
            toolbar={toolbar}
            className={className}
            style={style}
            loading={loading}
        >
            <TreeContainer {...props} />
        </StandardWidget>
    )
}

Widget.displayName = 'TreeWidgetComponent'

export const TreeWidget = WidgetHOC<TreeWidgetProps>(
    WithActiveModel<TreeWidgetProps>(Widget),
)
export default TreeWidget

TreeWidget.displayName = 'TreeWidget'
