import React from 'react'
import { useSelector } from 'react-redux'

import StandardWidget from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

import { Html } from './Html'
import { type HtmlWidgetProps } from './types'

function Widget({
    id,
    toolbar,
    className,
    style,
    html,
    loading,
    datasource,
}: HtmlWidgetProps) {
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)) as Array<Record<string, unknown>>
    const data = Array.isArray(datasourceModel) ? datasourceModel[0] : {} as Record<string, unknown>

    return (
        <StandardWidget
            widgetId={id}
            toolbar={toolbar}
            className={className}
            style={style}
            loading={loading}
            datasource={datasource}
        >
            <Html id={id} html={html} data={data} />
        </StandardWidget>
    )
}

Widget.displayName = 'HtmlWidgetComponent'

export const HtmlWidget = WidgetHOC<HtmlWidgetProps>(Widget)
export default HtmlWidget

HtmlWidget.displayName = 'HtmlWidget'
