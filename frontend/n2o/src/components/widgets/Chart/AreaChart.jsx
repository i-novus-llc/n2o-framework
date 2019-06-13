import React from 'react';
import { map } from 'lodash';
import {
  AreaChart as Chart,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  Area,
  Legend,
} from 'recharts';

function AreaChart({
  layout,
  width,
  height,
  stackOffset,
  baseValue,
  margin,
  XAxis: XAxisMetadata,
  YAxis: YAxisMetadata,
  cartesianGrid,
  tooltip,
  legend,
  data,
  areas,
}) {
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
      <XAxis dataKey="name" />
      <YAxis />
      <Tooltip {...tooltip} />
      {map(areas, area => (
        <Area {...area} />
      ))}
    </Chart>
  );
}

export default AreaChart;
