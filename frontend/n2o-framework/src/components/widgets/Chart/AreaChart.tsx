import React from 'react'
import map from 'lodash/map'
import {
    AreaChart as Chart,
    CartesianGrid,
    XAxis,
    YAxis,
    Tooltip,
    Area,
    Legend,
} from 'recharts'

import { parseData, createDomain } from './utils'
import { type Props } from './types'

function AreaChart({
    layout,
    stackOffset,
    baseValue,
    margin,
    XAxis: xaxis,
    YAxis: yaxis,
    cartesianGrid,
    tooltip,
    legend,
    data,
    areas,
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
            stackOffset={stackOffset}
            // @ts-ignore FIXME does not exist on type
            baseValue={baseValue}
        >
            <CartesianGrid {...cartesianGrid} />
            <Legend {...legend} />
            <XAxis {...xaxis} />
            <YAxis {...yaxis} domain={domain} />
            <Tooltip {...tooltip} />
            {map(parseData(areas), area => <Area {...area} />)}
        </Chart>
    )
}

export default AreaChart
