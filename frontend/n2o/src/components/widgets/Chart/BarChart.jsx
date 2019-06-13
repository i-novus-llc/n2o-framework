import React from 'react';
import { map } from 'lodash';
import {
  BarChart as Chart,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  Bar,
} from 'recharts';

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
  );
}

export default BarChart;
