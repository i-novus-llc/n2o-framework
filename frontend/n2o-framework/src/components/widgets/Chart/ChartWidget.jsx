import React, { useContext, useMemo } from 'react'
import PropTypes from 'prop-types'

import WidgetLayout from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import { WidgetHOC } from '../../../core/widget/Widget'
import { FactoryContext } from '../../../core/factory/context'
import { widgetPropTypes } from '../../../core/widget/propTypes'

import ChartType from './ChartType'
import LineChart from './LineChart'
import AreaChart from './AreaChart'
import BarChart from './BarChart'
import PieChart from './PieChart'

const Charts = {
    [ChartType.LINE]: LineChart,
    [ChartType.AREA]: AreaChart,
    [ChartType.BAR]: BarChart,
    [ChartType.PIE]: PieChart,
}

/**
 * Виджет графиков
 * @constructor
 */
function ChartWidget(props) {
    const {
        id: widgetId,
        datasource,
        toolbar,
        disabled,
        chart,
        models,
        filter,
        className,
        style,
        loading,
    } = props
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps])

    const { type } = chart

    const Component = Charts[type]

    return (
        <WidgetLayout
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
                <Component {...chart} data={models.datasource} />
            </div>
        </WidgetLayout>
    )
}

ChartWidget.propTypes = {
    ...widgetPropTypes,
    chart: PropTypes.arrayOf(PropTypes.shape({})),
}

export default WidgetHOC(ChartWidget)
