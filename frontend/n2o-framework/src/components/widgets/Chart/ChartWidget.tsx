import React, { useContext, useMemo } from 'react'
import { useSelector } from 'react-redux'

import StandardWidget, { type Props as StandardWidgetProps } from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { mapToNumeric } from '../../../tools/helpers'

import LineChart from './LineChart'
import AreaChart from './AreaChart'
import BarChart from './BarChart'
import PieChart from './PieChart'
import { CHART_TYPE, type DataItem, type ChartWidgetProps } from './types'

const Charts = {
    [CHART_TYPE.LINE]: LineChart,
    [CHART_TYPE.AREA]: AreaChart,
    [CHART_TYPE.BAR]: BarChart,
    [CHART_TYPE.PIE]: PieChart,
}

/**
 * Виджет графиков
 */
function Widget({
    id: widgetId,
    datasource,
    toolbar,
    disabled,
    chart,
    filter,
    className,
    style,
    loading,
}: ChartWidgetProps) {
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps]) as StandardWidgetProps['filter']
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)) as DataItem[]

    const { type } = chart

    const Component = Charts[type]

    const { width: propsWidth, height: propsHeight } = chart

    const { width, height } = mapToNumeric(
        {
            width: propsWidth,
            height: propsHeight,
        },
    )

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            toolbar={toolbar}
            filter={resolvedFilter}
            className={className}
            style={style}
            datasource={datasource}
            loading={loading}
        >
            <div className="n2o-chart-widget">
                <Component
                    {...chart}
                    width={width}
                    height={height}
                    data={datasourceModel}
                />
            </div>
        </StandardWidget>
    )
}

Widget.displayName = 'ChartWidgetComponent'

export const ChartWidget = WidgetHOC(Widget)
export default ChartWidget

ChartWidget.displayName = 'ChartWidget'
