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

import {
    barChartTypes,
    barsTypes,
    cartesianGridTypes,
    defaultChartProps,
    legendTypes,
    tooltipTypes,
    XAxisTypes,
    YAxisTypes,
} from './chartPropsTypes'

/**
 * График "Гистограмы"
 * @param layout - layout линий на графики
 * @param stackOffset
 * @param width - длина графика
 * @param height - высота графика
 * @param margin - отступы графика
 * @param barCategoryGap - расстояние между двумя категориями
 * @param barGap - расстояние  между двумя барами одной категории
 * @param barSize - длина или ширина каждого бара
 * @param maxBarSize - максимальная длина бара
 * @param reverseStackOrder - меняет порядок рендера баров
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
 * @param bars
 * [
 *  {
 *    stackId: id для настакивания баров друг на друга
 *    fill: заливка бара
 *    layout:
 *    dataKey: ключ в данных
 *    legendType: тип легенды
 *    label: лейбл
 *    barSize: размер бара
 *    maxBarSize: максимальная длина
 *    background: фон бара
 *  }
 * ]
 * @param data - данные
 * @return {*}
 * @constructor
 */
function BarChart({
    layout,
    width,
    height,
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
}) {
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
            <YAxis />
            <Tooltip {...tooltip} />
            <Legend {...legend} />
            {map(bars, bar => (
                <Bar {...bar} />
            ))}
        </Chart>
    )
}

BarChart.propTypes = {
    ...barChartTypes,
    cartesianGrid: cartesianGridTypes,
    XAxis: XAxisTypes,
    YAxis: YAxisTypes,
    tooltip: tooltipTypes,
    legend: legendTypes,
    bars: barsTypes,
}

BarChart.defaultProps = defaultChartProps

export default BarChart
