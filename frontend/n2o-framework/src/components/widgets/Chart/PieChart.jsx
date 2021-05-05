import React from 'react'
import { pure } from 'recompose'
import { PieChart as Chart, Pie, Tooltip, Cell } from 'recharts'
import get from 'lodash/get'
import map from 'lodash/map'

import { COLORS } from './utils'
import { chartTypes, defaultChartProps, pieTypes } from './chartPropsTypes'

/**
 * График "Пирог"
 * @param width - длина графика
 * @param height - высота графика
 * @param margin - отступы
 * @param pie
 * {
 *     cx: координата центра по x
 *     cy: координата центра по y
 *     innerRadius: внутренний радиус
 *     outerRadius: внешний радиус
 *     startAngle: начало пирога (в грудусах)
 *     endAngle: конец пирога (в градусах)
 *     minAngle: минимальный размер пирога
 *     paddingAngle: расстояние между секторами
 *     nameKey: ключ названия сектора
 *     dataKey: ключ значения сектора
 *     legendType: тип легенды
 *     label: подпись сектора
 *     labelLine: линия от сектора к подписи
 *     fill: заливка цветом
 *     animationBegin: дилей до начала анимации
 *     animationEasing: тип анимации
 *     size: Максимальное кол-во секторов
 * }
 * @param data
 * @param size
 * @return {*}
 * @constructor
 */

function PieChart({ width, height, margin, pie, data, size }) {
    const valueFieldId = get(pie, 'dataKey')
    const tooltipFieldId = get(pie, 'tooltipKey')
    const customFill = get(pie, 'fill')

    const mapData = valueFieldId
        ? map(data, elem => ({ ...elem, value: Number(elem[valueFieldId]) }))
        : data

    const pieData = mapData.slice(0, size)

    // eslint-disable-next-line react/prop-types
    const CustomTooltip = ({ payload }) => (
        <div className="n2o-pie-chart-custom-tooltip">
            <h4 className="n2o-pie-chart-custom-tooltip__content">
                {get(payload[0], `payload.payload.${tooltipFieldId}`)}
            </h4>
        </div>
    )

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
            {tooltipFieldId ? <Tooltip content={<CustomTooltip />} /> : <Tooltip />}
        </Chart>
    )
}

PieChart.propTypes = {
    ...chartTypes,
    pie: pieTypes,
}

PieChart.defaultProps = defaultChartProps

export default pure(PieChart)
