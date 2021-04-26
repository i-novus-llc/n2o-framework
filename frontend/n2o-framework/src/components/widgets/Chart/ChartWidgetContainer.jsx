import React from 'react'
import PropTypes from 'prop-types'
import { compose } from 'recompose'

import widgetContainer from '../WidgetContainer'
import { CHART } from '../widgetTypes'

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
 * Контейнер графика
 * @param chart - настройка графика
 * @param datasource - данные
 * @return {*}
 * @constructor
 */
function ChartWidgetContainer({ chart, datasource }) {
    const { type } = chart

    const Component = Charts[type]

    return (
        <div className="n2o-chart-widget">
            <Component {...chart} data={datasource} />
        </div>
    )
}

ChartWidgetContainer.propTypes = {
    chart: PropTypes.object,
    datasource: PropTypes.array,
}

ChartWidgetContainer.defaultProps = {
    chart: {},
    datasource: [],
}

export default compose(
    widgetContainer(
        {
            mapProps: props => ({
                chart: props.chart,
                datasource: props.datasource,
            }),
        },
        CHART,
    ),
)(ChartWidgetContainer)
