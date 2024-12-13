import React, { memo } from 'react'
import { PieChart as Chart, Pie, Tooltip, Cell } from 'recharts'
import get from 'lodash/get'
import map from 'lodash/map'

import { COLORS } from './utils'
import { type Props, type DataItem } from './types'

export const PieChart = memo(({
    margin,
    pie,
    data,
    size,
    width = 200,
    height = 200,
}: Props) => {
    const valueFieldId = get(pie, 'dataKey') as keyof DataItem
    const tooltipFieldId = get(pie, 'tooltipKey') as string
    const customFill = get(pie, 'fill') as string

    const mapData = valueFieldId
        ? map(data, elem => ({ ...elem, value: Number(elem[valueFieldId]) }))
        : data

    const pieData = mapData.slice(0, size)

    return (
        <Chart width={width} height={height} margin={margin}>
            <Pie
                {...pie}
                dataKey="value"
                nameKey="name"
                data={pieData}
                fill="#8884d8"
                label={pieMeta => pieMeta.name}
            >
                {pieData.map((entry, index) => (
                    <Cell
                        /* eslint-disable-next-line react/no-array-index-key */
                        key={`cell-${index}`}
                        fill={customFill || COLORS[index % COLORS.length]}
                    />
                ))}
            </Pie>
            {tooltipFieldId && <Tooltip />}
        </Chart>
    )
})

export default PieChart
