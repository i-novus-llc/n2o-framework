import React from 'react';
import PropTypes from 'prop-types';
import { map } from 'lodash';
import {
  LineChart as Chart,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  Line,
} from 'recharts';

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
  return (
    <Chart
      width={width}
      height={height}
      data={data}
      margin={margin}
      layout={layout}
    >
      <XAxis {...xaxis} />
      <YAxis />
      <CartesianGrid {...cartesianGrid} />
      <Tooltip {...tooltip} />
      <Legend {...legend} />
      {map(lines, line => (
        <Line {...line} />
      ))}
    </Chart>
  );
}

export default LineChart;
