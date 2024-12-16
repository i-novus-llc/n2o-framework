import React from 'react'
import map from 'lodash/map'
import {
    LineChart as Chart,
    CartesianGrid,
    XAxis,
    YAxis,
    Tooltip,
    Legend,
    Line,
} from 'recharts'

import { setLineColors, createDomain, parseData } from './utils'
import { type Props } from './types'

function LineChart({
    layout,
    margin,
    XAxis: xaxis,
    YAxis: yaxis,
    cartesianGrid,
    tooltip,
    legend,
    lines,
    data,
    width = 200,
    height = 200,
}: Props) {
    const domain = createDomain(yaxis)

    return (
        <Chart
            width={width}
            height={height}
            data={data}
            margin={margin}
            layout={layout}
        >
            <XAxis {...xaxis} />
            <YAxis {...yaxis} domain={domain} />
            <CartesianGrid {...cartesianGrid} />
            <Tooltip {...tooltip} />
            <Legend {...legend} />
            {map(setLineColors(parseData(lines)), line => <Line {...line} />)}
        </Chart>
    )
}

export default LineChart
