import React from 'react'
import map from 'lodash/map'
import {
    BarChart as Chart,
    CartesianGrid,
    XAxis,
    YAxis,
    Tooltip,
    Legend,
    Bar,
} from 'recharts'

import { parseData, createDomain } from './utils'
import { type Props } from './types'

function BarChart({
    layout,
    stackOffset,
    barCategoryGap,
    barGap,
    barSize,
    maxBarSize,
    reverseStackOrder,
    margin,
    XAxis: xaxis,
    YAxis: yaxis,
    cartesianGrid,
    tooltip,
    legend,
    bars,
    data,
    width = 200,
    height = 200,
}: Props) {
    const domain = createDomain(yaxis)

    return (
        <Chart
            layout={layout}
            width={width}
            height={height}
            stackOffset={stackOffset}
            barCategoryGap={barCategoryGap}
            barGap={barGap}
            barSize={barSize}
            maxBarSize={maxBarSize}
            reverseStackOrder={reverseStackOrder}
            data={data}
            margin={margin}
        >
            <CartesianGrid {...cartesianGrid} />
            <XAxis {...xaxis} />
            <YAxis {...yaxis} domain={domain} />
            <Tooltip {...tooltip} />
            <Legend {...legend} />
            {map(parseData(bars), bar => <Bar {...bar} />)}
        </Chart>
    )
}

export default BarChart
