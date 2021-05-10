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

import {
    cartesianGridTypes,
    chartTypes,
    defaultChartProps,
    legendTypes,
    linesTypes,
    tooltipTypes,
    XAxisTypes,
    YAxisTypes,
} from './chartPropsTypes'
import { setLineColors, createDomain, parseData } from './utils'

/**
 * График "Линии"
 * @param layout - layout линий на графики
 * @param width - длина графика
 * @param height - высота графика
 * @param margin - отступы графика
 * @param XAxis
 * {
 *  hide: флаг скрытия
 *  dataKey: ключ в данных
 *  height: высота
 *  width: длина
 *  orientation: ориентация оси
 *  type: тип оси
 *  allowDecimals: флаг разрешения float
 *  allowDataOverflow: флаг разрешения переполнения данных
 *  allowDuplicatedCategory: флаг разрешения повторябщихся категорий
 *  tickCount: количество делений оси
 *  interval:
 *  padding: отступы от оси
 *  minTickGap: минимальные отступы от tick оси
 *  axisLine:
 *  tickLine:
 *  tickSize:
 *  label: лейбл
 * }
 * @param YAxis
 * {
 *  hide: флаг скрытия
 *  dataKey: ключ в данных
 *  height: высота
 *  width: длина
 *  orientation: ориентация оси
 *  type: тип оси
 *  allowDecimals: флаг разрешения float
 *  allowDataOverflow: флаг разрешения переполнения данных
 *  allowDuplicatedCategory: флаг разрешения повторябщихся категорий
 *  tickCount: количество делений оси
 *  interval:
 *  padding: отступы от оси
 *  minTickGap: минимальные отступы от tick оси
 *  axisLine:
 *  tickLine:
 *  tickSize:
 *  label: лейбл
 * }
 * @param cartesianGrid
 * {
 *  x: координата x грида
 *  y: координата y грида
 *  width: длина грида
 *  height: высота грида
 *  horizontal: флаг отключения горизонтальных линий
 *  vertical: флаг отключения вертикальных линий
 *  horizontalPoints: координаты всех линий по y
 *  verticalPoints: координаты всех линий по x
 *  strokeDasharray: размеры dashed линий
 * }
 * @param tooltip
 * {
 *  separator: разделитель между названием и значением
 *  offset: расстояние между позицией тултипа и активной позицией
 *  filterNull: если значение айтема null | undefined
 *  itemStyle: стили айтема
 *  wrapperStyle: стили обертки
 *  contentStyle: стили контента
 *  labelStyle: стили лейбла
 *  viewBox:
 *  label: лейбл
 * }
 * @param legend
 * {
 *  width: длина легенды
 *  height: высота
 *  layout:
 *  align: горизонтальное выравнивание
 *  verticalAlign: вертикальное выравнивание
 *  iconSize: размер иконок
 *  iconType: тип иконок
 *  margin: отступы
 *  wrapperStyle: стили
 * }
 * @param lines
 * [
 *  {
 *    type: тип линии
 *    dataKey: ключ в данных
 *    stroke: цвет линии
 *    legendType: тип легенды
 *    dot: настройка точек
 *    activeDot: настройка активной точки
 *    label: лейбл
 *    layout:
 *  }
 * ]
 * @param data - данные
 * @return {*}
 * @constructor
 */

function LineChart({
    layout,
    width,
    height,
    margin,
    XAxis: xaxis,
    YAxis: yaxis,
    cartesianGrid,
    tooltip,
    legend,
    lines,
    data,
}) {
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
            {map(setLineColors(parseData(lines)), line => (
                <Line {...line} />
            ))}
        </Chart>
    )
}

LineChart.propTypes = {
    ...chartTypes,
    XAxis: XAxisTypes,
    YAxis: YAxisTypes,
    cartesianGrid: cartesianGridTypes,
    tooltip: tooltipTypes,
    legend: legendTypes,
    lines: linesTypes,
}

LineChart.defaultProps = defaultChartProps

export default LineChart
