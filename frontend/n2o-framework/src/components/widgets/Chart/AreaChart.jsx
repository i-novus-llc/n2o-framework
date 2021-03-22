import React from 'react';
import map from 'lodash/map';
import {
  AreaChart as Chart,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  Area,
  Legend,
} from 'recharts';
import {
  areasTypes,
  cartesianGridTypes,
  chartTypes,
  defaultChartProps,
  legendTypes,
  tooltipTypes,
  XAxisTypes,
  YAxisTypes,
} from './chartPropsTypes';
import { parseData, createDomain } from './utils';

/**
 * График "Зоны"
 * @param layout - layout линий на графики
 * @param stackOffset
 * @param baseValue - базовое значение зоны
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
 * @param areas
 * [
 *  {
 *    type: тип зоны
 *    dataKey: ключ в данных
 *    stroke: цвет зоны
 *    legendType: тип легенды
 *    dot: настройка точек
 *    activeDot: настройка активной точки
 *    label: лейбл
 *    layout:
 *    stackId: id для настакивания друг на друга
 *  }
 * ]
 * @param data - данные
 * @return {*}
 * @constructor
 */
function AreaChart({
  layout,
  width,
  height,
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
}) {
  const domain = createDomain(yaxis);

  return (
    <Chart
      width={width}
      height={height}
      data={data}
      margin={margin}
      layout={layout}
      stackOffset={stackOffset}
      baseValue={baseValue}
    >
      <CartesianGrid {...cartesianGrid} />
      <Legend {...legend} />
      <XAxis {...xaxis} />
      <YAxis {...yaxis} domain={domain} />
      <Tooltip {...tooltip} />
      {map(parseData(areas), area => (
        <Area {...area} />
      ))}
    </Chart>
  );
}

AreaChart.propTypes = {
  ...chartTypes,
  cartesianGrid: cartesianGridTypes,
  legend: legendTypes,
  XAxis: XAxisTypes,
  YAxis: YAxisTypes,
  tooltip: tooltipTypes,
  areas: areasTypes,
};

AreaChart.defaultProps = defaultChartProps;

export default AreaChart;
