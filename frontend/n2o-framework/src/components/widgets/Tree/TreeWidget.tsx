import React from 'react'

import StandardWidget, { type Props as StandardWidgetProps } from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { WithActiveModel } from '../Widget/WithActiveModel'

import TreeContainer from './container/TreeContainer'
import { type TreeProps, type WithWidgetHandlersProps } from './types'

function TreeWidget(props: StandardWidgetProps & TreeProps & WithWidgetHandlersProps) {
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
            <TreeContainer
                {...props}
            />
        </StandardWidget>
    )
}

export default WidgetHOC(WithActiveModel(TreeWidget as never))
