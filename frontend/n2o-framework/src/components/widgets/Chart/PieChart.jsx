import React from 'react'
import { pure } from 'recompose'
import { PieChart as Chart, Pie } from 'recharts'

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
 * }
 * @param data
 * @return {*}
 * @constructor
 */
function PieChart({ width, height, margin, pie, data }) {
    return (
        <Chart width={width} height={height} margin={margin}>
            <Pie {...pie} data={data} />
        </Chart>
    )
}

PieChart.propTypes = {
    ...chartTypes,
    pie: pieTypes,
}

PieChart.defaultProps = defaultChartProps

export default pure(PieChart)
